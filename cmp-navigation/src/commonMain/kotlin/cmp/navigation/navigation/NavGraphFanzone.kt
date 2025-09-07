package cmp.navigation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.oiid.core.config.artistId
import com.oiid.feature.fanzone.FanzoneDetailScreen
import com.oiid.feature.fanzone.FanzoneScreen
import com.oiid.feature.fanzone.list.FanzoneFeedViewModel
import com.oiid.feature.feed.detail.PostDetailArgs
import com.oiid.feature.feed.detail.navigateToForumPostDetail
import com.oiid.feature.feed.detail.postDetailArguments
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.fanzoneScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    composable(
        route = NavigationRoutes.Fanzone.NavigationRoute.route,
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) },
        popExitTransition = { fadeOut(animationSpec = tween(300)) },
    ) {
        FanzoneScreen(
            modifier = modifier,
            viewModel = koinViewModel<FanzoneFeedViewModel>(),
            onPostClicked = { postId ->
                navController.navigateToForumPostDetail(postId, artistId())
            },
            onNavigateToEditProfile = {
                navController.navigateToProfile()
            },
        )
    }

    composable(
        arguments = postDetailArguments,
        route = NavigationRoutes.Fanzone.Detail.route,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth },)
        },
        exitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) },
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<PostDetailArgs>()

        FanzoneDetailScreen(
            modifier = modifier,
            postId = args.postId,
            artistId = args.artistId,
            onBackClick = { navController.popBackStack() },
            onNavigateToEditProfile = {
                navController.navigateToProfile()
            },
        )
    }
}
