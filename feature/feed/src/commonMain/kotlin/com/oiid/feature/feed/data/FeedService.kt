package com.oiid.feature.feed.data

import com.oiid.core.model.Post
import com.oiid.core.model.PostComment
import com.oiid.core.model.PostItem
import com.oiid.core.model.api.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface FeedService {
    fun getFeed(): Flow<Resource<List<PostItem>>>
    suspend fun loadData()
    fun getCachedFeedSnapshot(): List<PostItem>?
    fun getFeedItemFromCache(id: String): PostItem?
    suspend fun toggleLikePost(artistId: String, postId: String): Boolean
}

interface PostService {
    fun getPost(): StateFlow<Resource<Post>>

    /**
     * Posts a new comment to a specific post.
     * @param artistId The unique identifier for the artist.
     * @param postId The unique identifier for the post.
     * @param content The comment to post.
     * @return The posted [PostComment].
     */
    suspend fun postComment(artistId: String, postId: String, content: String): PostComment

    /**
     * Posts a reply to a specific comment.
     * @param artistId The unique identifier for the artist.
     * @param postId The unique identifier for the post.
     * @param commentId The unique identifier for the parent comment.
     * @param content The reply to comment.
     * @return The posted [PostComment].
     */
    suspend fun postCommentReply(artistId: String, postId: String, commentId: String, content: String): PostComment

    /**
     * Likes a specific post.
     * @param artistId The unique identifier for the artist.
     * @param postId The unique identifier for the post.
     */
    suspend fun likePost(artistId: String, postId: String)

    /**
     * Toggles like status for a specific comment.
     * @param artistId The unique identifier for the artist.
     * @param postId The unique identifier for the post.
     * @param commentId The unique identifier for the comment.
     * @return True if the operation was successful, false otherwise.
     */
    suspend fun toggleLikeComment(artistId: String, postId: String, commentId: String): Boolean

    /**
     * Reports a specific comment.
     * @param artistId The unique identifier for the artist.
     * @param postId The unique identifier for the post.
     * @param commentId The unique identifier for the comment.
     * @return True if the operation was successful, false otherwise.
     */
    suspend fun reportComment(artistId: String, postId: String, commentId: String): Boolean
}
