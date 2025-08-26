package cmp.navigation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) },
        popExitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        EventsScreen(
            modifier = modifier,
            appBar = appBar,
        )
    }
}