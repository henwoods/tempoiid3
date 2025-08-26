package com.oiid.core.datastore

import co.touchlab.kermit.Logger
import com.oiid.core.model.Post
import com.oiid.core.model.PostComment
import com.oiid.core.model.PostItem
import com.oiid.core.model.api.Resource
import com.oiid.core.model.api.feed.CreateCommentRequest
import com.oiid.core.model.toPostComment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class PostServiceImpl(
    private val config: PostServiceAdapter,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val postId: String,
    private val artistId: String,
) : PostService {

    private val postStateFlow = MutableStateFlow<Resource<List<PostComment>>>(Resource.Loading)
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val cacheDelegate = PostCacheDelegate(
        postId = postId,
        cacheKeyPrefix = config.cacheKeyPrefix,
        tag = config.tag,
    )

    private val postItemFlow: Flow<Resource<PostItem>> = channelFlow {
        val cachedItem = config.getPostFromCache(postId)
        val cachedComments = cacheDelegate.getCommentsFromCache()

        if (cachedItem != null && cachedComments != null) {
            send(Resource.Success(cachedItem))
        } else {
            send(Resource.Loading)
        }

        config.getPosts().collect { feedResource ->
            when (feedResource) {
                is Resource.Loading -> {
                    if (cachedItem == null) {
                        send(Resource.Loading)
                    }
                }

                is Resource.Error -> send(Resource.Error(feedResource.exception))
                is Resource.Success -> {
                    val postItem = feedResource.data.find { it.id == postId }
                    if (postItem != null) {
                        send(Resource.Success(postItem, cachedComments == null))
                    } else {
                        if (cachedItem == null) {
                            send(Resource.Error(Exception("Post not found")))
                        }
                    }
                }
            }
        }
    }.distinctUntilChanged()

    init {
        coroutineScope.launch {
            loadPost()
        }
    }

    override suspend fun loadPost() {
        val previous = postStateFlow.value
        val existingComments = if (previous is Resource.Success) previous.data else emptyList()

        if (existingComments.isEmpty()) {
            postStateFlow.value = Resource.Loading
        }

        try {
            val remotePost = config.getPostComments(artistId, postId)
            Logger.d { "${config.tag} Successfully loaded remote post $postId ${remotePost.size}" }
            cacheDelegate.updateCache(remotePost)
            postStateFlow.value = Resource.Success(remotePost)
        } catch (e: Exception) {
            Logger.e("${config.tag} Error loading remote post data: ${e.message}")
            val cachedItems = cacheDelegate.getCommentsFromCache()
            if (!cachedItems.isNullOrEmpty()) {
                Logger.d { "${config.tag} Network failed. Serving stale data from cache" }
                postStateFlow.value = Resource.Success(cachedItems)
            } else {
                Logger.w { "${config.tag} Network failed and cache is empty. Emitting error state." }
                postStateFlow.value = Resource.Error(e)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    final override fun getPost(): StateFlow<Resource<Post>> {
        return postItemFlow.combine(postStateFlow) { postResource, commentsResource ->
            when (postResource) {
                is Resource.Loading -> Resource.Loading
                is Resource.Error -> Resource.Error(postResource.exception)
                is Resource.Success -> {
                    when (commentsResource) {
                        is Resource.Loading -> Resource.Success(Post(postResource.data, emptyList()), true)
                        is Resource.Error -> Resource.Error(commentsResource.exception)
                        is Resource.Success -> Resource.Success(Post(postResource.data, commentsResource.data))
                    }
                }
            }
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Resource.Loading,
        )
    }

    final override suspend fun createComment(artistId: String, postId: String, content: String): PostComment {
        return try {
            val postedComment = config.createComment(artistId, postId, CreateCommentRequest(content)).toPostComment()
            cacheDelegate.addCommentToCache(postedComment)
            val updatedComments = cacheDelegate.getCommentsFromCache() ?: listOf(postedComment)
            postStateFlow.value = Resource.Success(updatedComments)

            loadPost()

            postedComment
        } catch (e: Exception) {
            Logger.e("${config.tag} Error posting comment: ${e.message}")
            throw e
        }
    }

    final override suspend fun postCommentReply(
        artistId: String,
        postId: String,
        commentId: String,
        content: String,
    ): PostComment {
        return try {
            val postedReply =
                config.createCommentReply(artistId, postId, commentId, CreateCommentRequest(content)).toPostComment()
            Logger.d { "${config.tag} Successfully posted reply to comment $commentId" }

            cacheDelegate.addReplyToCache(commentId, postedReply)
            val updatedComments = cacheDelegate.getCommentsFromCache() ?: emptyList()
            postStateFlow.value = Resource.Success(updatedComments)

            loadPost()

            postedReply
        } catch (e: Exception) {
            Logger.e("${config.tag} Error posting reply: ${e.message}")
            throw e
        }
    }

    final override suspend fun likePost(artistId: String, postId: String) {
        try {
            config.likePost(artistId, postId)
            Logger.d { "${config.tag} Successfully liked post $postId" }

            loadPost()

            try {
                config.getPost(artistId, postId)
                Logger.d { "${config.tag} Successfully fetched updated post data after like action" }

                val cachedItem = config.getPostFromCache(postId)
                if (cachedItem != null) {
                    Logger.d { "${config.tag} Post found in feed cache, it will be updated on next feed refresh" }
                }
            } catch (e: Exception) {
                Logger.e("${config.tag} Error fetching updated post data after like: ${e.message}")
            }
        } catch (e: Exception) {
            Logger.e("${config.tag} Error liking post: ${e.message}")
            throw e
        }
    }

    final override suspend fun toggleLikeComment(artistId: String, postId: String, commentId: String): Boolean {
        try {
            val likedCommentIds = userPreferencesRepository.getLikedComments().getOrDefault(emptySet())
            val isLiked = commentId in likedCommentIds
            val comment = cacheDelegate.findCommentInCache(commentId)

            if (isLiked || (comment?.numberOfLikes ?: 0) > 0) {
                config.unlikeComment(artistId, postId, commentId)
            } else {
                config.likeComment(artistId, postId, commentId)
            }

            if (isLiked) {
                userPreferencesRepository.removeLikedComment(commentId)
            } else {
                userPreferencesRepository.addLikedComment(commentId)
            }

            if (comment != null) {
                cacheDelegate.updateCommentLikeInCache(commentId, !isLiked)
                val updatedComments = cacheDelegate.getCommentsFromCache() ?: emptyList()

                if (postStateFlow.value is Resource.Success) {
                    postStateFlow.value = Resource.Success(updatedComments)
                }
            }

            return true
        } catch (e: Exception) {
            Logger.e("${config.tag} Error updating comment like: ${e.message}")
            return false
        }
    }

    final override suspend fun reportPost(artistId: String, postId: String): Boolean {
        return config.reportPost(artistId, postId)
    }

    final override suspend fun reportComment(artistId: String, postId: String, commentId: String): Boolean {
        return try {
            config.reportComment(artistId, postId, commentId)
            Logger.d { "${config.tag} Successfully reported comment $commentId" }
            true
        } catch (e: Exception) {
            Logger.e("${config.tag} Error reporting comment: ${e.message}")
            false
        }
    }

    final override fun getPostFromCache(postId: String): PostItem? {
        return config.getPostFromCache(postId)
    }

    final override suspend fun updatePost(artistId: String, postId: String, title: String, content: String): PostItem? {
        return try {
            val updatedPost = config.updatePost(artistId, postId, title, content)
            Logger.d { "${config.tag} Successfully updated post $postId" }
            loadPost()
            updatedPost
        } catch (e: Exception) {
            Logger.e("${config.tag} Error updating post: ${e.message}")
            null
        }
    }
}
