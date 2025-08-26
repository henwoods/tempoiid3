package com.oiid.core.datastore

import com.oiid.core.model.PostComment
import com.oiid.core.model.PostCommentResponse
import com.oiid.core.model.PostItem
import com.oiid.core.model.api.Resource
import com.oiid.core.model.api.feed.CreateCommentRequest
import kotlinx.coroutines.flow.Flow

/**
 * Adapter interface that provides access to the underlying services.
 * This allows the PostServiceImpl to work with different underlying services and APIs
 * by directly accessing the service and apiService properties.
 */
interface PostServiceAdapter {
    /**
     * The tag used for logging purposes
     */
    val tag: String

    /**
     * The cache key prefix used for caching comments
     */
    val cacheKeyPrefix: String

    fun getPosts(): Flow<Resource<List<PostItem>>>
    fun getPostFromCache(postId: String): PostItem?

    suspend fun getPostComments(artistId: String, postId: String): List<PostComment>
    suspend fun createComment(artistId: String, postId: String, request: CreateCommentRequest): PostCommentResponse
    suspend fun createCommentReply(artistId: String, postId: String, commentId: String, request: CreateCommentRequest): PostCommentResponse
    suspend fun likePost(artistId: String, postId: String)
    suspend fun getPost(artistId: String, postId: String): PostItem
    suspend fun likeComment(artistId: String, postId: String, commentId: String)
    suspend fun unlikeComment(artistId: String, postId: String, commentId: String)
    suspend fun reportPost(artistId: String, postId: String): Boolean
    suspend fun reportComment(artistId: String, postId: String, commentId: String)
    suspend fun updatePost(artistId: String, postId: String, title: String, content: String): PostItem?

    suspend fun updateFeedCacheAfterLike(postId: String)
    suspend fun updateFeedCacheAfterComment(postId: String)
}
