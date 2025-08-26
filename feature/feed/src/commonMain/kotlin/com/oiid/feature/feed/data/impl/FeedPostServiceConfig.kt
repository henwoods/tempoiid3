package com.oiid.feature.feed.data.impl

import com.oiid.core.datastore.PostServiceAdapter
import com.oiid.core.model.PostComment
import com.oiid.core.model.PostCommentResponse
import com.oiid.core.model.PostItem
import com.oiid.core.model.api.Resource
import com.oiid.core.model.api.feed.CreateCommentRequest
import com.oiid.core.datastore.FeedService
import com.oiid.network.api.PostApiService
import kotlinx.coroutines.flow.Flow

class FeedPostServiceAdapter(
    private val feedService: FeedService,
    private val postApiService: PostApiService,
) : PostServiceAdapter {

    override val tag: String = "PostServiceImpl"
    override val cacheKeyPrefix: String = "POST_CACHE"

    override fun getPosts(): Flow<Resource<List<PostItem>>> {
        return feedService.getPosts()
    }

    override fun getPostFromCache(postId: String): PostItem? {
        return feedService.getPostFromCache(postId)
    }

    override suspend fun getPostComments(artistId: String, postId: String): List<PostComment> {
        return postApiService.getPostComments(artistId, postId)
    }

    override suspend fun createComment(artistId: String, postId: String, request: CreateCommentRequest): PostCommentResponse {
        return postApiService.createComment(artistId, postId, request)
    }

    override suspend fun createCommentReply(artistId: String, postId: String, commentId: String, request: CreateCommentRequest): PostCommentResponse {
        return postApiService.createCommentReply(artistId, postId, commentId, request)
    }

    override suspend fun likePost(artistId: String, postId: String) {
        postApiService.likePost(artistId, postId)
    }

    override suspend fun getPost(artistId: String, postId: String): PostItem {
        return postApiService.getPost(artistId, postId)
    }

    override suspend fun likeComment(artistId: String, postId: String, commentId: String) {
        postApiService.likeComment(artistId, postId, commentId)
    }

    override suspend fun unlikeComment(artistId: String, postId: String, commentId: String) {
        postApiService.unlikeComment(artistId, postId, commentId)
    }

    override suspend fun reportPost(artistId: String, postId: String): Boolean {
        throw UnsupportedOperationException("Can't report a post on the feed")
    }

    override suspend fun reportComment(artistId: String, postId: String, commentId: String) {
        postApiService.reportComment(artistId, postId, commentId)
    }

    override suspend fun updatePost(artistId: String, postId: String, title: String, content: String): PostItem? {
        return feedService.updatePost(postId, title, content)
    }
}
