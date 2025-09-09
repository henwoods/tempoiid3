package cmp.navigation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import cmp.navigation.utils.TopLevelDestination
import com.oiid.feature.settings.ProfileScreen

fun NavController.navigateToProfile(navOptions: NavOptions? = null) =
    navigate(NavigationRoutes.Profile.NavigationRoute.route, navOptions)

fun NavController.navigateToProfileTab() {
    val topLevelNavOptions = navOptions {
        popUpTo(NavGraphRoute.MAIN_GRAPH) {
            saveState = true
            inclusive = false
        }

        launchSingleTop = true
        restoreState = true
    }
    navigate(NavigationRoutes.Profile.NavigationRoute.route, topLevelNavOptions)
}

fun NavGraphBuilder.profileScreen(
    onLogout: () -> Unit,
) {
    composable(
        route = NavigationRoutes.Profile.NavigationRoute.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        ProfileScreen(onLogoutClick = onLogout)
    }
}
