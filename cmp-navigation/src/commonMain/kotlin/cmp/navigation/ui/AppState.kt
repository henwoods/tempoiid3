package cmp.navigation.ui

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.util.trace
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import cmp.navigation.navigation.NavGraphRoute
import cmp.navigation.navigation.NavigationRoutes
import cmp.navigation.navigation.navigateToProfile
import cmp.navigation.screens.navigateToComingSoon
import cmp.navigation.utils.TopLevelDestination
import com.oiid.core.data.utils.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun rememberAppState(
    networkMonitor: NetworkMonitor,
    windowSizeClass: WindowSizeClass = calculateWindowSizeClass(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): AppState {
    return remember(
        navController,
        coroutineScope,
        windowSizeClass,
        networkMonitor,
    ) {
        AppState(
            navController = navController,
            coroutineScope = coroutineScope,
            windowSizeClass = windowSizeClass,
            networkMonitor = networkMonitor,
        )
    }
}

@Stable
class AppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            NavigationRoutes.Feed.List.route -> TopLevelDestination.Home
            NavigationRoutes.Feed.Detail.route -> TopLevelDestination.Home
            NavigationRoutes.Profile.NavigationRoute.route -> TopLevelDestination.Profile
            NavigationRoutes.Fanzone.NavigationRoute.route -> TopLevelDestination.Fanzone
            NavigationRoutes.Events.NavigationRoute.route -> TopLevelDestination.Events
            NavigationRoutes.Library.NavigationRoute.route -> TopLevelDestination.Library
            NavigationRoutes.Merch.NavigationRoute.route -> TopLevelDestination.Merch

            else -> null
        }

    val shouldShowBottomBar: Boolean = true
    //   get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(NavGraphRoute.MAIN_GRAPH) {
                    saveState = true
                    inclusive = false
                }

                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }

            when (topLevelDestination) {
                TopLevelDestination.Home -> navController.navigate(NavigationRoutes.Feed.List.route, topLevelNavOptions)
                TopLevelDestination.Library -> navController.navigateToComingSoon(NavigationRoutes.Library.NavigationRoute.route, topLevelNavOptions)
                TopLevelDestination.Events -> navController.navigateToComingSoon(NavigationRoutes.Events.NavigationRoute.route, topLevelNavOptions)
                TopLevelDestination.Fanzone -> navController.navigateToComingSoon(NavigationRoutes.Fanzone.NavigationRoute.route, topLevelNavOptions)
                TopLevelDestination.Profile -> navController.navigateToProfile(topLevelNavOptions)
                TopLevelDestination.Merch -> navController.navigateToComingSoon(NavigationRoutes.Merch.NavigationRoute.route, topLevelNavOptions)
            }
        }
    }
}
