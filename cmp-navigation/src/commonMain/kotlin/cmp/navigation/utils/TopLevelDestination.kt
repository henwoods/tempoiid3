package cmp.navigation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import cmp.navigation.generated.resources.Res
import cmp.navigation.generated.resources.events
import cmp.navigation.generated.resources.fanzone
import cmp.navigation.generated.resources.home
import cmp.navigation.generated.resources.library
import cmp.navigation.generated.resources.profile
import cmp.navigation.generated.resources.store
import cmp.navigation.navigation.NavigationRoutes
import com.oiid.core.designsystem.generated.resources.catalog
import com.oiid.core.designsystem.generated.resources.fanzone
import com.oiid.core.designsystem.generated.resources.profile
import com.oiid.core.designsystem.generated.resources.store
import com.oiid.core.designsystem.generated.resources.tickets
import com.oiid.core.model.ui.TabItem
import com.oiid.core.oiidPainterResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource

enum class TopLevelDestination(
    val selectedIcon: @Composable () -> Painter,
    val unselectedIcon: @Composable () -> Painter,
    val iconText: StringResource,
    val titleText: StringResource,
    val route: String,
) {
    Library(
        selectedIcon = { painterResource(com.oiid.core.designsystem.generated.resources.Res.drawable.catalog) },
        unselectedIcon = { painterResource(com.oiid.core.designsystem.generated.resources.Res.drawable.catalog) },
        iconText = Res.string.library,
        titleText = Res.string.library,
        route = "library",
    ),
    Events(
        selectedIcon = {
            painterResource(
                com.oiid.core.designsystem.generated.resources.Res.drawable.tickets,
            )
        },
        unselectedIcon = {
            painterResource(
                com.oiid.core.designsystem.generated.resources.Res.drawable.tickets,
            )
        },
        iconText = Res.string.events,
        titleText = Res.string.events,
        route = "events",
    ),
    Home(
        selectedIcon = { oiidPainterResource("home_tab_icon") },
        unselectedIcon = { oiidPainterResource("home_tab_icon") },
        iconText = Res.string.home,
        titleText = Res.string.home,
        route = "feed",
    ),
    Fanzone(
        selectedIcon = {
            painterResource(
                com.oiid.core.designsystem.generated.resources.Res.drawable.fanzone,
            )
        },
        unselectedIcon = {
            painterResource(
                com.oiid.core.designsystem.generated.resources.Res.drawable.fanzone,
            )
        },
        iconText = Res.string.fanzone,
        titleText = Res.string.fanzone,
        route = "fanzone",
    ),
    Merch(
        selectedIcon = {
            painterResource(
                com.oiid.core.designsystem.generated.resources.Res.drawable.store,
            )
        },
        unselectedIcon = {
            painterResource(
                com.oiid.core.designsystem.generated.resources.Res.drawable.store,
            )
        },
        iconText = Res.string.store,
        titleText = Res.string.store,
        route = "merch",
    ),
    Profile(
        selectedIcon = {
            painterResource(
                com.oiid.core.designsystem.generated.resources.Res.drawable.profile,
            )
        },
        unselectedIcon = {
            painterResource(
                com.oiid.core.designsystem.generated.resources.Res.drawable.profile,
            )
        },
        iconText = Res.string.profile,
        titleText = Res.string.profile,
        route = "profile",
    ),
}

data class Destinations(
    val destinations: List<TopLevelDestination>,
    val profileOverflows: Boolean,
)

@Composable
fun rememberDestinations(
    allDestinations: List<TopLevelDestination>,
    availableTabs: List<TabItem>,
): Destinations {
    return remember(allDestinations, availableTabs) {
        calculateDestinations(
            allDestinations = allDestinations,
            availableTabs = availableTabs,
        )
    }
}

fun calculateDestinations(
    allDestinations: List<TopLevelDestination>,
    availableTabs: List<TabItem>,
): Destinations {
    val tabRoutes = availableTabs.map { it.route }.toSet()
    val visibleDestinations = allDestinations.filter { it.route in tabRoutes }

    val overflow = visibleDestinations.size % 2 == 0

    val finalDestinations = if (overflow) {
        visibleDestinations.filterNot {
            it.route == NavigationRoutes.Profile.NavigationRoute.route
        }
    } else {
        visibleDestinations
    }

    return Destinations(
        destinations = finalDestinations,
        profileOverflows = overflow,
    )
}
