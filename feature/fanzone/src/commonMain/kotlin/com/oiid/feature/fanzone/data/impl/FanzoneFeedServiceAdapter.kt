package com.oiid.feature.fanzone.data.impl

import com.oiid.core.datastore.FeedServiceAdapter
import com.oiid.core.model.PostItem
import com.oiid.core.model.api.feed.CreatePostRequest
import com.oiid.core.model.toPostItem
import com.oiid.network.api.FanzoneApiService

class FanzoneFeedServiceAdapter(
    private val fanzoneApiService: FanzoneApiService,
) : FeedServiceAdapter {

    override val tag: String = "FanzoneService"
    override val cacheKey: String = "ALL_FORUM_POSTS"

    override suspend fun getPosts(artistId: String): List<PostItem> {
        return fanzoneApiService.getPosts(artistId).items.map { it.toPostItem() }
    }

    override suspend fun likePost(artistId: String, postId: String) {
        fanzoneApiService.likePost(artistId, postId)
    }

    override suspend fun unlikePost(artistId: String, postId: String) {
        fanzoneApiService.unlikePost(artistId, postId)
    }

    override suspend fun createPost(artistId: String, request: CreatePostRequest): PostItem? {
        return fanzoneApiService.createPost(artistId, request)
    }

    override suspend fun updatePost(artistId: String, postId: String, request: CreatePostRequest): PostItem? {
        return fanzoneApiService.updatePost(artistId, postId, request)
    }

    override suspend fun reportPost(artistId: String, postId: String): Boolean {
        return try {
            fanzoneApiService.flagPost(artistId, postId)
            true
        } catch (_: Exception) {
            false
        }
    }
}
