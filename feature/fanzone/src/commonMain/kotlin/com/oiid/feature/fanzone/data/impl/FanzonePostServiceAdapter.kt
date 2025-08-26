package com.oiid.feature.fanzone.data.impl

import com.oiid.core.datastore.FeedService
import com.oiid.core.datastore.PostServiceAdapter
import com.oiid.core.model.PostComment
import com.oiid.core.model.PostCommentResponse
import com.oiid.core.model.PostItem
import com.oiid.core.model.api.Resource
import com.oiid.core.model.api.feed.CreateCommentRequest
import com.oiid.network.api.FanzoneApiService
import kotlinx.coroutines.flow.Flow

class FanzonePostServiceAdapter(
    private val fanzoneService: FeedService,
    private val fanzoneApiService: FanzoneApiService,
) : PostServiceAdapter {

    override val tag: String = "ForumPostServiceImpl"
    override val cacheKeyPrefix: String = "FORUM_POST_CACHE"

    override fun getPosts(): Flow<Resource<List<PostItem>>> {
        return fanzoneService.getPosts()
    }

    override fun getPostFromCache(postId: String): PostItem? {
        return fanzoneService.getPostFromCache(postId)
    }

    override suspend fun getPostComments(artistId: String, postId: String): List<PostComment> {
        return fanzoneApiService.getPostComments(artistId, postId)
    }

    override suspend fun createComment(
        artistId: String,
        postId: String,
        request: CreateCommentRequest,
    ): PostCommentResponse {
        return fanzoneApiService.createComment(artistId, postId, request)
    }

    override suspend fun createCommentReply(
        artistId: String,
        postId: String,
        commentId: String,
        request: CreateCommentRequest,
    ): PostCommentResponse {
        return fanzoneApiService.createCommentReply(artistId, postId, commentId, request)
    }

    override suspend fun likePost(artistId: String, postId: String) {
        fanzoneApiService.likePost(artistId, postId)
    }

    override suspend fun getPost(artistId: String, postId: String): PostItem {
        return fanzoneApiService.getPost(artistId, postId)
    }

    override suspend fun likeComment(artistId: String, postId: String, commentId: String) {
        fanzoneApiService.likeComment(artistId, postId, commentId)
    }

    override suspend fun unlikeComment(artistId: String, postId: String, commentId: String) {
        fanzoneApiService.unlikeComment(artistId, postId, commentId)
    }

    override suspend fun reportPost(artistId: String, postId: String): Boolean {
        return try {
            fanzoneApiService.flagPost(artistId, postId)
            true
        } catch (_: Exception) {
            false
        }
    }

    override suspend fun reportComment(artistId: String, postId: String, commentId: String) {
        fanzoneApiService.reportComment(artistId, postId, commentId)
    }

    override suspend fun updatePost(artistId: String, postId: String, title: String, content: String): PostItem? {
        return fanzoneService.updatePost(postId, title, content)
    }

    override suspend fun updateFeedCacheAfterLike(postId: String) {
        fanzoneService.toggleLikePost(postId)
    }

    override suspend fun updateFeedCacheAfterComment(postId: String) {
        fanzoneService.loadPosts()
    }
}
