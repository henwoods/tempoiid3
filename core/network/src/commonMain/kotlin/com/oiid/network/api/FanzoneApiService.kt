package com.oiid.network.api

import com.oiid.core.model.ForumPostsResponse
import com.oiid.core.model.PostComment
import com.oiid.core.model.PostCommentResponse
import com.oiid.core.model.PostItem
import com.oiid.core.model.api.feed.CreateCommentRequest
import com.oiid.core.model.api.feed.CreatePostRequest
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path

interface FanzoneApiService {
    @GET("app/artists/{artistId}/feed/{postId}")
    suspend fun getPost(@Path("artistId") artistId: String, @Path("postId") postId: String): PostItem

    @GET("app/artists/{artistId}/forums/posts")
    suspend fun getPosts(@Path("artistId") artistId: String): ForumPostsResponse

    @POST("app/artists/{artistId}/forums/posts")
    suspend fun createPost(
        @Path("artistId") artistId: String,
        @Body request: CreatePostRequest,
    ): PostItem

    @PUT("app/artists/{artistId}/forums/posts/{postId}")
    suspend fun updatePost(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
        @Body request: CreatePostRequest,
    ): PostItem

    @POST("app/artists/{artistId}/forums/posts/{postId}/flag")
    suspend fun flagPost(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
    )

    @POST("app/artists/{artistId}/forums/posts/{postId}/likes")
    suspend fun likePost(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
    )

    @DELETE("app/artists/{artistId}/forums/posts/{postId}/likes")
    suspend fun unlikePost(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
    )

    @GET("app/artists/{artistId}/forums/posts/{postId}/comments")
    suspend fun getPostComments(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
    ): List<PostComment>

    @POST("app/artists/{artistId}/forums/posts/{postId}/comments")
    suspend fun createComment(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
        @Body request: CreateCommentRequest,
    ): PostCommentResponse

    @POST("app/artists/{artistId}/forums/posts/{postId}/comments/{commentId}/comments")
    suspend fun createCommentReply(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
        @Path("commentId") commentId: String,
        @Body request: CreateCommentRequest,
    ): PostCommentResponse

    @POST("app/artists/{artistId}/forums/posts/{postId}/comments/{commentId}/flag")
    suspend fun reportComment(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
        @Path("commentId") commentId: String,
    )

    @POST("app/artists/{artistId}/forums/posts/{postId}/comments/{commentId}/likes")
    suspend fun likeComment(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
        @Path("commentId") commentId: String,
    )

    @DELETE("app/artists/{artistId}/forums/posts/{postId}/comments/{commentId}/likes")
    suspend fun unlikeComment(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
        @Path("commentId") commentId: String,
    )
}
