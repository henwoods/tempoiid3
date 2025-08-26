package cmp.navigation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import cmp.navigation.SplashViewModel
import cmp.navigation.navigation.NavigationRoutes
import co.touchlab.kermit.Logger
import com.oiid.core.model.AuthState
import oiid.core.base.designsystem.AppStateViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * This screen attempts to smooths the "loading blur". It is "displayed" (it has no content)
 *  - first thing on a cold start
 *  - after sign in
 *
 * It decides if the blur foreground should be displayed based on whether there is cached data. If the user us
 * unauthenticated or authentication is unknown it will potentially navigate to authentication or return doing nothing.
 *
 */
@Composable
fun NavWrapperScreen(
    authState: AuthState,
    navController: NavHostController,
    splashViewModel: SplashViewModel = koinViewModel(),
    appStateViewModel: AppStateViewModel = koinViewModel(),
) {
    val cacheIsEmpty = splashViewModel.cacheIsEmpty.collectAsState()

    LaunchedEffect(authState) {
        Logger.withTag("Splash").d { "authState: $authState cached empty: ${cacheIsEmpty.value}" }

        // If the auth state is unknown, do nothing.
        if (authState == AuthState.Refreshing) {
            Logger.withTag("Splash").d { "Authentication state unknown, skipping composition" }
            return@LaunchedEffect
        }

        if (authState == AuthState.Unauthenticated) {
            navController.navigate(NavigationRoutes.Unauthenticated.NavigationRoute.route) {
                Logger.withTag("Splash").d { "Navigating to Auth" }
                popUpTo(NavigationRoutes.NavWrapper.Splash.route) {
                    inclusive = true
                }
            }
            return@LaunchedEffect
        }

        // If the feed cache is empty, display the loading blur
        if (cacheIsEmpty.value) {
            Logger.withTag("Splash").d { "Enabling foreground blur" }
            appStateViewModel.setForegroundBlur("Loading")
        } else {
            // TODO: if the user logs out and logs back in, the cache won't be empty, so it can incorrectly clear the
            //  foreground here. Need to clear the cache on logout.
            appStateViewModel.setForegroundBlur(null)
        }

        // Navigate to the feed, or to authentication depending on the auth state
        navController.navigate(NavigationRoutes.Feed.NavigationRoute.route) {
            Logger.withTag("Splash").d { "Navigating to Feed" }
            popUpTo(NavigationRoutes.NavWrapper.Splash.route) {
                inclusive = true
            }
        }
    }
}
