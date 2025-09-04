package cmp.navigation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.oiid.core.config.artistId
import com.oiid.feature.feed.HomeFeedScreen
import com.oiid.feature.feed.detail.PostDetailArgs
import com.oiid.feature.feed.detail.PostDetailScreen
import com.oiid.feature.feed.detail.navigateToPostDetail
import com.oiid.feature.feed.detail.postDetailArguments

fun NavGraphBuilder.feedGraph(
    modifier: Modifier,
    navController: NavController,
    appBar: @Composable () -> Unit,
) {
    navigation(
        route = NavigationRoutes.Feed.NavigationRoute.route,
        startDestination = NavigationRoutes.Feed.List.route,
    ) {
        composable(
            route = NavigationRoutes.Feed.List.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
            popEnterTransition = { fadeIn(animationSpec = tween(300)) },
            popExitTransition = { fadeOut(animationSpec = tween(300)) },
        ) {
            HomeFeedScreen(
                modifier = modifier,
                appBar = appBar,
                onPostClicked = { postId ->
                    navController.navigateToPostDetail(postId, artistId())
                },
            )
        }

        composable(
            arguments = postDetailArguments,
            route = NavigationRoutes.Feed.Detail.route,
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<PostDetailArgs>()

            PostDetailScreen(
                modifier = modifier,
                postId = args.postId,
                artistId = args.artistId,
                isForum = true,
                onBackClick = { navController.popBackStack() },
                onNavigateToEditProfile = {
                    navController.navigateToProfile()
                },
            )
        }
    }
}
