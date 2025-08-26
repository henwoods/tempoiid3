package com.oiid.core.datastore

import com.oiid.core.model.Post
import com.oiid.core.model.PostComment
import com.oiid.core.model.PostItem
import com.oiid.core.model.api.Resource
import kotlinx.coroutines.flow.StateFlow

interface PostService {
    suspend fun loadPost()

    fun getPost(): StateFlow<Resource<Post>>

    suspend fun createComment(artistId: String, postId: String, content: String): PostComment
    suspend fun postCommentReply(artistId: String, postId: String, commentId: String, content: String): PostComment
    suspend fun likePost(artistId: String, postId: String)
    suspend fun toggleLikeComment(artistId: String, postId: String, commentId: String): Boolean
    suspend fun reportPost(artistId: String, postId: String): Boolean
    suspend fun reportComment(artistId: String, postId: String, commentId: String): Boolean
    fun getPostFromCache(postId: String): PostItem?
    suspend fun updatePost(artistId: String, postId: String, title: String, content: String): PostItem?
}
