package cmp.navigation.ui

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import cmp.navigation.utils.TopLevelDestination
import com.oiid.core.designsystem.components.OiidNavigationBar
import com.oiid.core.designsystem.components.OiidNavigationBarItem
import oiid.core.base.designsystem.theme.OiidTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    destinations: List<TopLevelDestination>,
    destinationsWithUnreadResources: Set<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
) {
    OiidNavigationBar(modifier = modifier.height(84.dp)) {
        destinations.forEach { destination ->
            val hasUnread = destinationsWithUnreadResources.contains(destination)
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)

            OiidNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        painter = destination.unselectedIcon(),
                        contentDescription = null,
                    )
                },
                modifier = if (hasUnread) Modifier.notificationDot() else Modifier,
                selectedIcon = {
                    Icon(
                        painter = destination.selectedIcon(),
                        contentDescription = null,
                    )
                },
                label = {
                    if (destination.route != TopLevelDestination.Home.route) {
                        Text(
                            style = OiidTheme.typography.labelSmall,
                            text = stringResource(destination.iconText),
                        )
                    }
                },
            )
        }
    }
}

private fun Modifier.notificationDot(): Modifier = composed {
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    drawWithContent {
        drawContent()
        drawCircle(
            tertiaryColor,
            radius = 5.dp.toPx(),
            // This is based on the dimensions of the NavigationBar's "indicator pill";
            // however, its parameters are private, so we must depend on them implicitly
            // (NavigationBarTokens.ActiveIndicatorWidth = 64.dp)
            center = center + Offset(
                64.dp.toPx() * .45f,
                32.dp.toPx() * -.45f - 6.dp.toPx(),
            ),
        )
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) = this?.hierarchy?.any {
    it.route?.contains(destination.route, true) == true
} == true
