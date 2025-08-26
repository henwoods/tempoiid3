package com.oiid.feature.feed.detail

import androidx.navigation.NavController
import androidx.navigation.NavOptions

fun NavController.navigateToPostDetail(
    postId: String,
    artistId: String,
    navOptions: NavOptions? = null,
) {
    val route = "detail/$postId/$artistId"
    navigate(route, navOptions)
}

fun NavController.navigateToForumPostDetail(
    postId: String,
    artistId: String,
    navOptions: NavOptions? = null,
) {
    val route = "forum/$postId/$artistId"
    navigate(route, navOptions)
}
