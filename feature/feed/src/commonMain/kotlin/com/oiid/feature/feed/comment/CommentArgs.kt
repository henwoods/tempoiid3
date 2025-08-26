package com.oiid.feature.feed.comment

import kotlinx.serialization.Serializable

@Serializable
data class CommentArgs(val feedItemId: Int, val parentCommentId: Int? = null)
