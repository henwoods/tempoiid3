package com.oiid.core.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Post(val postItem: PostItem, val comments: List<PostComment>)

@Serializable
data class PostItem(
    val id: String = "",
    val artistId: String = "",
    val userId: String = "",
    val title: String = "",
    val content: String = "",
    val imageURLs: List<String> = emptyList(),
    val creationDate: String = "",
    val createdAt: Instant = Instant.DISTANT_PAST,
    val updatedAt: Instant = Instant.DISTANT_PAST,
    val lastActivityDate: String? = null,
    val numberOfLikes: Int = 0,
    val numberOfComments: Int = 0,
    val numberOfFlags: Int = 0,
    val numberOfHiddenComments: Int = 0,
    val isPinned: Boolean = false,
    val isLikedByUser: Boolean = false,
    val isPostByUser: Boolean = false,
    val flagged: Boolean = false,
    val removed: Boolean = false,
    val name: String = "",
    val profileImage: String = "",
    val isSuperfan: Boolean = false,
    val isBandAffiliate: Boolean = false,
)

fun PostCommentResponse.toPostComment(): PostComment {
    return PostComment(
        commentId = commentId,
        userId = userId,
        postId = postId,
        content = content,
        removed = removed,
        parentCommentId = parentCommentId,
        numberOfLikes = numberOfLikes,
        createdAt = Instant.fromEpochMilliseconds(createdAt),
        updatedAt = Instant.fromEpochMilliseconds(updatedAt),
        flagged = flagged,
        name = name,
        isBandAffiliate = isBandAffiliate,
        comments = comments.map { it },
        profileImage = profileImage,
    )
}

@Serializable
data class PostCommentResponse(
    val commentId: String = "",
    val userId: String = "",
    val postId: String? = null,
    val content: String = "",
    val removed: Boolean = false,
    val parentCommentId: String? = null,
    val numberOfLikes: Int = 0,
    val createdAt: Long = 0,
    val updatedAt: Long = 0,
    val flagged: Boolean = false,
    val name: String = "",
    val isBandAffiliate: Boolean = false,
    val comments: List<PostComment> = emptyList(),
    val profileImage: String = "",
)

@Serializable
data class PostComment(
    val commentId: String = "",
    val userId: String = "",
    val postId: String? = null,
    val content: String = "",
    val removed: Boolean = false,
    val parentCommentId: String? = null,
    val numberOfLikes: Int = 0,
    val isLikedByUser: Boolean = false,
    val createdAt: Instant = Instant.DISTANT_PAST,
    val updatedAt: Instant = Instant.DISTANT_PAST,
    val flagged: Boolean = false,
    val name: String = "",
    val isBandAffiliate: Boolean = false,
    val isSuperfan: Boolean = false,
    val comments: List<PostComment> = emptyList(),
    val profileImage: String = "",
)

@Serializable
data class ForumPostResponse(
    @SerialName("id") val id: String,
    @SerialName("artistId") val artistId: String,
    @SerialName("userId") val userId: String,
    @SerialName("content") val content: String,
    @SerialName("title") val title: String,
    @SerialName("lastActivityDate") val lastActivityDate: String,
    @SerialName("createdAt") val createdAt: Instant,
    @SerialName("updatedAt") val updatedAt: Instant,
    @SerialName("numberOfLikes") val numberOfLikes: Int,
    @SerialName("numberOfComments") val numberOfComments: Int,
    @SerialName("numberOfFlags") val numberOfFlags: Int,
    @SerialName("flagged") val flagged: Boolean,
    @SerialName("removed") val removed: Boolean,
    @SerialName("numberOfHiddenComments") val numberOfHiddenComments: Int,
    @SerialName("isPinned") val isPinned: Boolean,
    @SerialName("name") val name: String? = null,
    @SerialName("profileImage") val profileImage: String? = null,
    @SerialName("isBandAffiliate") val isBandAffiliate: Boolean = false,
)

@Serializable
data class ForumPostsResponse(
    @SerialName("items") val items: List<ForumPostResponse>,
)

fun ForumPostResponse.toPostItem(): PostItem {
    return PostItem(
        id = id,
        artistId = artistId,
        title = title,
        content = content,
        userId = userId,
        imageURLs = emptyList(),
        createdAt = createdAt,
        updatedAt = createdAt,
        lastActivityDate = lastActivityDate,
        numberOfLikes = numberOfLikes,
        numberOfComments = numberOfComments,
        numberOfFlags = numberOfFlags,
        numberOfHiddenComments = numberOfHiddenComments,
        isPinned = isPinned,
        isLikedByUser = false,
        flagged = flagged,
        removed = removed,
        name = name ?: "",
        profileImage = profileImage ?: "",
        isBandAffiliate = isBandAffiliate,
    )
}

enum class PostType {
    TEXT,
    IMAGE,
    VIDEO,
    COMBINED_MEDIA,
}

// This extension property determines the post type based on its content
val PostItem.postType: PostType
    get() {
        val hasText = content.isNotBlank()
        val hasMedia = imageURLs.isNotEmpty()

        return when {
            hasMedia && imageURLs.first().endsWith(".mp4") -> PostType.VIDEO
            hasText && hasMedia -> PostType.COMBINED_MEDIA
            !hasText && hasMedia -> PostType.IMAGE
            else -> PostType.TEXT
        }
    }
