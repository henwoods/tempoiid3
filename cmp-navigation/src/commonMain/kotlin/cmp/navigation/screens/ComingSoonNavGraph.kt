package cmp.navigation.screens

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import cmp.navigation.navigation.NavigationRoutes

fun NavController.navigateToComingSoon(route: String, navOptions: NavOptions? = null) =
    navigate(route, navOptions)

fun NavGraphBuilder.comingSoonScreen() {
    val routes = listOf(
        NavigationRoutes.Merch.NavigationRoute.route,
        NavigationRoutes.Library.NavigationRoute.route,
    )

    routes.forEach { route ->
        composable(
            route = route,
            enterTransition = {
                EnterTransition.None
            },
            exitTransition = { ExitTransition.None },
        ) {
            ComingSoon()
        }
    }
}
