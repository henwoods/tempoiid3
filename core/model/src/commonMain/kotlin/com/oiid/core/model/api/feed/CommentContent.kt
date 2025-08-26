package com.oiid.core.model.api.feed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRequest(
    @SerialName("title") val title: String,
    @SerialName("content") val content: String,
)

@Serializable
data class CreateCommentRequest(
    @SerialName("content") val content: String,
)
