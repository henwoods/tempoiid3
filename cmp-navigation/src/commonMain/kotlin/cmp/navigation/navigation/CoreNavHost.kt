package cmp.navigation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cmp.navigation.screens.NavWrapperScreen
import cmp.navigation.screens.comingSoonScreen
import co.touchlab.kermit.Logger
import com.oiid.core.model.AuthState
import com.oiid.feature.auth.AuthViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun FeatureNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    appBar: @Composable () -> Unit,
    inDarkTheme: Boolean,
    authViewModel: AuthViewModel = koinViewModel(),
) {
    val authState by authViewModel.isAuthenticated.collectAsState()

    LaunchedEffect(authState) {
        Logger.withTag("FeatureNavHost").d { "LaunchedEffect: $authState" }
        var isInitial = true
        authViewModel.isAuthenticated
            .onEach { state ->
                if (isInitial) {
                    isInitial = false
                    Logger.withTag("FeatureNavHost").d { "Initial auth state: $state" }
                    return@onEach
                }

                Logger.withTag("FeatureNavHost").d { "Auth state changed to: $state" }
                when (state) {
                    AuthState.Authenticated -> {
                        navController.navigate(NavigationRoutes.NavWrapper.Splash.route) {
                            popUpTo(NavigationRoutes.Unauthenticated.NavigationRoute.route) {
                                inclusive = true
                            }
                        }
                    }

                    AuthState.Unauthenticated -> {
                        navController.navigate(NavigationRoutes.Unauthenticated.NavigationRoute.route) {
                            popUpTo(NavigationRoutes.Feed.NavigationRoute.route) {
                                inclusive = true
                            }
                        }
                    }

                    else -> Unit
                }
            }.launchIn(this)
    }

    val startDestination = when (authState) {
        AuthState.Authenticated -> NavigationRoutes.NavWrapper.Splash.route
        AuthState.Unauthenticated -> NavigationRoutes.Unauthenticated.NavigationRoute.route
        else -> NavigationRoutes.NavWrapper.Splash.route
    }

    NavHost(
        route = NavGraphRoute.MAIN_GRAPH,
        startDestination = startDestination,
        navController = navController,
        modifier = modifier,
    ) {
        composable(NavigationRoutes.NavWrapper.Splash.route) {
            NavWrapperScreen(authState = authState, navController = navController)
        }

        unauthenticatedGraph(navController)

        onboardingGraph(navController)

        feedGraph(modifier, inDarkTheme, navController, appBar)

        profileScreen(
            onLogout = {
                navController.popBackStack(NavigationRoutes.Profile.NavigationRoute.route, true)
            },
        )

        eventsScreen(modifier, appBar)

        fanzoneScreen(modifier, navController)

        comingSoonScreen()
    }
}
