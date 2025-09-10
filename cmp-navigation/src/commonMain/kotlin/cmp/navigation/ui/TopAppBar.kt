package cmp.navigation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import cmp.navigation.navigation.navigateToProfile
import cmp.navigation.utils.Destinations
import cmp.navigation.utils.TopLevelDestination
import com.oiid.core.designsystem.composable.OiidHeader
import com.oiid.core.oiidPainterResource
import com.oiid.feature.feed.ProfileIconButton
import oiid.core.base.designsystem.theme.OiidTheme.spacing
import oiid.core.base.designsystem.theme.ThemedStatusBar

@Composable
fun ArtistAppBar(
    modifier: Modifier = Modifier,
    isDark: Boolean,
    destination: TopLevelDestination?,
    destinationInfo: Destinations,
    appState: AppState,
) {
    Column(modifier.fillMaxSize()) {
        if (destination != null) {
            AppBar(
                title = {
                    if (destination.route == TopLevelDestination.Home.route) {
                        Image(
                            painter = oiidPainterResource(
                                if (isDark) {
                                    "artist_header_dark"
                                } else {
                                    "artist_header_light"
                                },
                            ),
                            contentDescription = "Artist Header",
                            modifier = Modifier.size(172.dp, 56.dp),
                        )
                    } else if (destination.route == TopLevelDestination.Events.route) {
                        OiidHeader(title = "Events".uppercase())
                    }
                },
                overflowProfile = destinationInfo.profileOverflows,
                onNavigateToSettings = {
                    appState.navController.navigateToProfile()
                },
                destination = appState.currentTopLevelDestination,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: @Composable () -> Unit,
    overflowProfile: Boolean = false,
    onNavigateToSettings: () -> Unit,
    destination: TopLevelDestination?,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = {
            title()
        },
        actions = {
            Box {
                if (overflowProfile) {
                    when (destination) {
                        TopLevelDestination.Home -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(spacing.xs)) {
                                ProfileIconButton(onNavigateToProfile = onNavigateToSettings)
                            }
                        }

                        else -> {}
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        modifier = modifier.testTag("topAppBar"),
    )
}
