package cmp.navigation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.oiid.feature.events.EventsScreen

fun NavGraphBuilder.eventsScreen(
    modifier: Modifier = Modifier,
    appBar: @Composable () -> Unit,
) {
    composable(
        route = NavigationRoutes.Events.NavigationRoute.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        EventsScreen(
            modifier = modifier,
            appBar = appBar,
        )
    }
}