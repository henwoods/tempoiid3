package cmp.navigation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.navigation.toRoute
import cmp.navigation.navigation.NavigationRoutes.Companion.LOGIN_CONFIRMED
import com.oiid.core.config.oiidTheme
import com.oiid.core.designsystem.theme.OiidTheme
import com.oiid.core.model.nav.ConfirmationStateArgs
import com.oiid.feature.auth.AuthScreen
import com.oiid.feature.auth.SignInScreen
import com.oiid.feature.auth.SignUpScreen

fun NavGraphBuilder.unauthenticatedGraph(navController: NavController) {
    navigation(
        route = NavigationRoutes.Unauthenticated.NavigationRoute.route,
        startDestination = NavigationRoutes.Unauthenticated.Auth.route,
    ) {
        composable(
            route = NavigationRoutes.Unauthenticated.Auth.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
        ) {
            OiidTheme(oiidColorScheme = oiidTheme(), true) {
                AuthScreen(
                    onNavigateToRegistration = {
                        navController.navigate(route = NavigationRoutes.Unauthenticated.Registration.route)
                    },
                    onNavigateToSignIn = {
                        navController.navigate(route = NavigationRoutes.Unauthenticated.SignIn.route)
                    },
                )
            }
        }

        composable(
            route = "$LOGIN_CONFIRMED={confirmed}",
            arguments = listOf(
                navArgument("confirmed") {
                    type = NavType.BoolType
                    defaultValue = false
                },
            ),
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<ConfirmationStateArgs>()
            OiidTheme(oiidColorScheme = oiidTheme(), true) {
                SignInScreen(
                    onBackClick = navController::navigateUp,
                    confirmationState = args,
                )
            }
        }

        composable(route = NavigationRoutes.Unauthenticated.Registration.route) {
            OiidTheme(oiidColorScheme = oiidTheme(), true) {
                SignUpScreen(
                    onBackClick = navController::navigateUp,
                    onConfirmedLogin = {
                        navController.navigate(route = NavigationRoutes.Unauthenticated.SignIn.createRoute(true)) {
                            popUpTo(route = NavigationRoutes.Unauthenticated.NavigationRoute.route) {
                                inclusive = true
                            }
                        }
                    },
                )
            }
        }
    }
}
