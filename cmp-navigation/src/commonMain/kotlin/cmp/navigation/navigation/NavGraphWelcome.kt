package cmp.navigation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.oiid.feature.auth.OnboardingScreen

fun NavGraphBuilder.onboardingGraph(navController: NavController) {
    navigation(
        route = NavigationRoutes.Onboarding.NavigationRoute.route,
        startDestination = NavigationRoutes.Onboarding.Slider.route,
    ) {
        composable(route = NavigationRoutes.Onboarding.Slider.route) {
            OnboardingScreen(onDismiss = { navController.navigate(NavigationRoutes.Feed.List.route) })
        }
    }
}
