package com.oiid.feature.feed.data.impl

import com.oiid.core.model.PostItem
import com.oiid.core.model.api.Resource
import com.oiid.feature.feed.data.FeedService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import oiid.core.base.datastore.cache.CacheManager
import oiid.core.base.datastore.cache.LruCacheManager
import cmp_oiid.feature.feed.generated.resources.Res

private const val FEED_ITEMS_KEY = "ALL_FEED_ITEMS"
private const val LOCAL_JSON_PATH = "files/feed_data.json"

class LocalFeedServiceImpl(
    private val cache: CacheManager<String, PostItem> = LruCacheManager(),
    private val feedItemListCache: CacheManager<String, List<PostItem>> = LruCacheManager(),
) : FeedService {
    private val feedFlow = MutableStateFlow<Resource<List<PostItem>>>(Resource.Loading)
    private val json = Json { ignoreUnknownKeys = true }
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    init {
        val cachedItems = getAllFeedItems()
        if (cachedItems.isNotEmpty()) {
            feedFlow.value = Resource.Success(cachedItems)
            cachedItems.forEach { cache.put(it.id, it) }
        }

        coroutineScope.launch {
            val jsonItems = loadLocalJsonData()
            if (jsonItems != null) {
                feedFlow.value = Resource.Success(jsonItems)
                jsonItems.forEach { cache.put(it.id, it) }
            }
        }
    }

    private suspend fun loadLocalJsonData(): List<PostItem>? {
        return try {
            val jsonBytes = Res.readBytes(LOCAL_JSON_PATH)
            val jsonString = jsonBytes.decodeToString()
            val feedItems = json.decodeFromString<List<PostItem>>(jsonString)
            // Store in cache for future use
            feedItemListCache.put(FEED_ITEMS_KEY, feedItems)
            feedItems
        } catch (e: Exception) {
            println("Error loading local JSON data: ${e.message}")
            null
        }
    }

    private fun getAllFeedItems(): MutableList<PostItem> {
        return (feedItemListCache.get(FEED_ITEMS_KEY) ?: emptyList()).toMutableList()
    }

    private fun updateFeed(feedListItems: List<PostItem>) {
        feedItemListCache.clear()
        feedItemListCache.put(FEED_ITEMS_KEY, feedListItems)
        cache.clear()
        feedListItems.forEach { cache.put(it.id, it) }
        feedFlow.value = Resource.Success(feedListItems)
    }

    override fun getFeed(): Flow<Resource<List<PostItem>>> {
        return feedFlow
    }

    override suspend fun loadData() {
        TODO("Not yet implemented")
    }

    override fun getCachedFeedSnapshot(): List<PostItem>? {
        TODO("Not yet implemented")
    }

    override fun getFeedItemFromCache(id: String): PostItem? {
        TODO("Not yet implemented")
    }

    override suspend fun toggleLikePost(artistId: String, postId: String): Boolean {
        return false
    }
}
