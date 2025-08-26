package com.oiid.core.datastore

import com.oiid.core.model.PostItem
import com.oiid.core.model.api.Resource
import kotlinx.coroutines.flow.Flow

interface FeedService {
    suspend fun loadPosts()

    fun getPosts(): Flow<Resource<List<PostItem>>>
    fun getCachedPostsSnapshot(): List<PostItem>?
    fun getPostFromCache(id: String): PostItem?

    suspend fun createPost(title: String, content: String): PostItem?
    suspend fun updatePost(postId: String, title: String, content: String): PostItem?
    suspend fun toggleLikePost(postId: String): Boolean
    suspend fun reportPost(postId: String): Boolean
}
