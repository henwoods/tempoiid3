package com.oiid.network.api

import com.oiid.core.model.PostComment
import com.oiid.core.model.PostCommentResponse
import com.oiid.core.model.PostItem
import com.oiid.core.model.api.feed.CreateCommentRequest
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface PostApiService {
    @GET("app/artists/{artistId}/feed/{postId}")
    suspend fun getPost(@Path("artistId") artistId: String, @Path("postId") postId: String): PostItem

    @GET("app/artists/{artistId}/feed/{postId}/comments")
    suspend fun getPostComments(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
    ): List<PostComment>

    @GET("app/artists/{artistId}/feed/{postId}/comments/{commentId}/comments")
    suspend fun getCommentReplies(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
        @Path("commentId") commentId: Int,
    ): List<PostComment>

    @POST("app/artists/{artistId}/feed/{postId}/comments")
    suspend fun createComment(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
        @Body content: CreateCommentRequest,
    ): PostCommentResponse

    @POST("app/artists/{artistId}/feed/{postId}/comments/{commentId}/comments")
    suspend fun createCommentReply(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
        @Path("commentId") commentId: String,
        @Body content: CreateCommentRequest,
    ): PostCommentResponse

    @POST("app/artists/{artistId}/feed/{postId}/likes")
    suspend fun likePost(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
    )

    @DELETE("app/artists/{artistId}/feed/{postId}/likes")
    suspend fun unlikePost(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
    )

    @POST("app/artists/{artistId}/feed/{postId}/comments/{commentId}/likes")
    suspend fun likeComment(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
        @Path("commentId") commentId: String,
    )

    @DELETE("app/artists/{artistId}/feed/{postId}/comments/{commentId}/likes")
    suspend fun unlikeComment(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
        @Path("commentId") commentId: String,
    )

    @POST("app/artists/{artistId}/feed/{postId}/comments/{commentId}/flag")
    suspend fun reportComment(
        @Path("artistId") artistId: String,
        @Path("postId") postId: String,
        @Path("commentId") commentId: String,
    )
}
