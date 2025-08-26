package cmp.navigation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
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
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}
