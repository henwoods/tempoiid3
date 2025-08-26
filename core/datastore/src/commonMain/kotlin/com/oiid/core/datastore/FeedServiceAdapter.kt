package com.oiid.core.datastore

import com.oiid.core.model.PostItem
import com.oiid.core.model.api.feed.CreatePostRequest

/**
 * Adapter interface that provides access to the underlying API services.
 * This allows the FeedServiceImpl to work with different underlying API services
 * by delegating to the appropriate service methods.
 */
interface FeedServiceAdapter {
    /**
     * The tag used for logging purposes
     */
    val tag: String

    /**
     * The cache key used for caching posts
     */
    val cacheKey: String

    suspend fun getPosts(artistId: String): List<PostItem>
    suspend fun likePost(artistId: String, postId: String)
    suspend fun unlikePost(artistId: String, postId: String)
    suspend fun createPost(artistId: String, request: CreatePostRequest): PostItem?
    suspend fun updatePost(artistId: String, postId: String, request: CreatePostRequest): PostItem?
    suspend fun reportPost(artistId: String, postId: String): Boolean
}