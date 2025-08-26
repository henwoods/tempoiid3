package com.oiid.feature.feed.data.impl

import com.oiid.core.datastore.FeedServiceAdapter
import com.oiid.core.model.PostItem
import com.oiid.core.model.api.feed.CreatePostRequest
import com.oiid.network.api.FeedApiService
import com.oiid.network.api.PostApiService

class FeedItemsServiceAdapter(
    private val feedApiService: FeedApiService,
    private val postApiService: PostApiService,
) : FeedServiceAdapter {

    override val tag: String = "FeedService"
    override val cacheKey: String = "ALL_FEED_ITEMS"

    override suspend fun getPosts(artistId: String): List<PostItem> {
        return feedApiService.getPosts(artistId)
    }

    override suspend fun likePost(artistId: String, postId: String) {
        postApiService.likePost(artistId, postId)
    }

    override suspend fun unlikePost(artistId: String, postId: String) {
        postApiService.unlikePost(artistId, postId)
    }

    override suspend fun createPost(artistId: String, request: CreatePostRequest): PostItem? {
        throw UnsupportedOperationException("Can't create post in feed")
    }

    override suspend fun updatePost(artistId: String, postId: String, request: CreatePostRequest): PostItem? {
        throw UnsupportedOperationException("Can't update post in feed")
    }

    override suspend fun reportPost(artistId: String, postId: String): Boolean {
        throw UnsupportedOperationException("Can't report post in feed")
    }
}
