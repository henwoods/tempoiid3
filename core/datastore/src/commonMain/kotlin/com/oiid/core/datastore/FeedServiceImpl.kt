package com.oiid.core.datastore

import co.touchlab.kermit.Logger
import com.oiid.core.model.PostItem
import com.oiid.core.model.api.Resource
import com.oiid.core.model.api.feed.CreatePostRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import oiid.core.base.datastore.cache.CacheManager
import oiid.core.base.datastore.cache.LruCacheManager
import oiid.core.base.datastore.exceptions.InvalidKeyException

abstract class FeedServiceImpl(
    private val config: FeedServiceAdapter,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val artistId: String,
    private val cache: CacheManager<String, PostItem> = LruCacheManager(),
    private val postsCache: CacheManager<String, List<PostItem>> = LruCacheManager(),
) : FeedService {

    private val postsStateFlow = MutableStateFlow<Resource<List<PostItem>>>(Resource.Loading)
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    init {
        coroutineScope.launch {
            loadPosts()
        }
    }

    override fun getPosts(): Flow<Resource<List<PostItem>>> {
        return postsStateFlow
    }

    override suspend fun loadPosts() {
        postsStateFlow.value = Resource.Loading
        val likedPostIds = userPreferencesRepository.getLikedPosts().getOrDefault(emptySet())
        val currentUserId = userPreferencesRepository.exportPreferences().getOrNull()?.userId ?: ""

        val cachedItems = getAllFeedItemsFromCache()
        if (cachedItems.isNotEmpty()) {
            val cachedItemsWithLikedStatus = cachedItems.map { feedItem ->
                feedItem.copy(
                    isLikedByUser = likedPostIds.contains(feedItem.id),
                    isPostByUser = feedItem.userId == currentUserId,
                )
            }

            Logger.withTag(config.tag).d {
                "Serving stale data from cache: ${cachedItemsWithLikedStatus.size} items"
            }
            postsStateFlow.value = Resource.Success(cachedItemsWithLikedStatus)
        }

        try {
            val posts = config.getPosts(artistId)

            val feedItemsWithLikedStatus = posts.map { feedItem ->
                feedItem.copy(
                    isLikedByUser = likedPostIds.contains(feedItem.id),
                    isPostByUser = feedItem.userId == currentUserId,
                )
            }

            Logger.withTag(config.tag).d { "Successfully loaded remote feed data: ${feedItemsWithLikedStatus.size} items" }
            updateCache(feedItemsWithLikedStatus)
            postsStateFlow.value = Resource.Success(feedItemsWithLikedStatus)
        } catch (e: Exception) {
            Logger.withTag(config.tag).e("Error loading remote feed data: ${e.message}")

            if (e.message?.contains("The incoming token has expired") == true) {
                postsStateFlow.value = Resource.Error(InvalidKeyException("User was signed out"))
            } else {
                Logger.withTag(config.tag).w { "Network failed and cache is empty. Emitting error state." }
                postsStateFlow.value = Resource.Error(e)
            }
        }
    }

    override fun getCachedPostsSnapshot(): List<PostItem>? {
        return postsCache.get(config.cacheKey)
    }

    private fun getAllFeedItemsFromCache(): List<PostItem> {
        return getCachedPostsSnapshot() ?: emptyList()
    }

    private fun updateCache(feedListItems: List<PostItem>) {
        postsCache.put(config.cacheKey, feedListItems)
        cache.clear()
        feedListItems.forEach { cache.put(it.id, it) }
    }

    override fun getPostFromCache(id: String): PostItem? {
        return try {
            cache.get(id) ?: postsCache.get(config.cacheKey)?.find { it.id == id }
        } catch (e: Exception) {
            Logger.withTag(config.tag).w(e) { "Failed to get cached post: $id" }
            null
        }
    }

    override suspend fun toggleLikePost(postId: String): Boolean {
        return try {
            val likedPostIds = userPreferencesRepository.getLikedPosts().getOrDefault(emptySet())
            val isLiked = postId in likedPostIds

            if (isLiked) {
                config.unlikePost(artistId, postId)
            } else {
                config.likePost(artistId, postId)
            }

            if (isLiked) {
                userPreferencesRepository.removeLikedPost(postId)
            } else {
                userPreferencesRepository.addLikedPost(postId)
            }

            val updatedItems = getAllFeedItemsFromCache().map { feedItem ->
                if (feedItem.id != postId) return@map feedItem

                feedItem.copy(
                    numberOfLikes = feedItem.numberOfLikes + if (isLiked) -1 else 1,
                    isLikedByUser = !isLiked,
                    isPostByUser = feedItem.isPostByUser,
                )
            }

            updateCache(updatedItems)

            if (postsStateFlow.value is Resource.Success) {
                postsStateFlow.value = Resource.Success(updatedItems)
            }

            Logger.withTag(config.tag).d { "Toggled like for post: $postId" }
            true
        } catch (e: Exception) {
            Logger.withTag(config.tag).e(e) { "Failed to toggle like for post: $postId, ${e.message}" }
            false
        }
    }

    override suspend fun reportPost(postId: String): Boolean {
        return try {
            val result = config.reportPost(artistId, postId)

            if (result) {
                val cachedPost = getPostFromCache(postId)
                cachedPost?.let { post ->
                    val updatedPost = post.copy(
                        flagged = true,
                        isPostByUser = post.isPostByUser,
                    )
                    cache.put(postId, updatedPost)
                }
                Logger.withTag(config.tag).d { "Flagged post: $postId" }
            }

            result
        } catch (e: Exception) {
            Logger.withTag(config.tag).e(e) { "Failed to flag post: $postId" }
            false
        }
    }

    override suspend fun createPost(title: String, content: String): PostItem? {
        return try {
            val request = CreatePostRequest(title = title, content = content)
            val newPost = config.createPost(artistId, request)

            newPost?.let { post ->
                cache.put(post.id, post)
                loadPosts()
                Logger.withTag(config.tag).d { "Created new post: ${post.id}" }
            }

            newPost
        } catch (e: Exception) {
            Logger.withTag(config.tag).e(e) { "Failed to create post" }
            null
        }
    }

    override suspend fun updatePost(postId: String, title: String, content: String): PostItem? {
        return try {
            val request = CreatePostRequest(title = title, content = content)
            val updatedPost = config.updatePost(artistId, postId, request)

            updatedPost?.let { post ->
                cache.put(post.id, post)
                loadPosts()
                Logger.withTag(config.tag).d { "Updated post: $postId" }
            }

            updatedPost
        } catch (e: Exception) {
            Logger.withTag(config.tag).e(e) { "Failed to update post: $postId" }
            null
        }
    }
}
