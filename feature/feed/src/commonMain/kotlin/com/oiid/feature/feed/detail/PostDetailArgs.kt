package com.oiid.feature.feed.detail

import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.serialization.Serializable

@Serializable
data class PostDetailArgs(
    val postId: String,
    val artistId: String,
)

val postDetailArguments = listOf(
    navArgument("postId") { type = NavType.StringType },
    navArgument("artistId") { type = NavType.StringType },
)
