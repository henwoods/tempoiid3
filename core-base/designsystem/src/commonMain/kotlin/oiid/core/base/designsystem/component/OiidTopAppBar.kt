@file:OptIn(ExperimentalMaterial3Api::class)

package oiid.core.base.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import oiid.core.base.designsystem.core.OiidTopAppBarConfiguration
import oiid.core.base.designsystem.core.TopAppBarVariant
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.base.designsystem.theme.OiidTheme.typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OiidTopAppBar(configuration: OiidTopAppBarConfiguration) {
    val finalModifier = configuration.modifier
        .testTag(configuration.testTag ?: "OiidTopAppBar")
        .let { mod ->
            if (configuration.contentDescription != null) {
                mod.semantics { contentDescription = configuration.contentDescription }
            } else {
                mod
            }
        }

    val titleContent: @Composable () -> Unit = {
        Column {
            Text(
                text = configuration.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            configuration.subtitle?.let { subtitle ->
                Text(
                    text = subtitle,
                    style = typography.bodySmall,
                    color = colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }

    val navigationIconContent: @Composable () -> Unit = {
        configuration.navigationIcon?.let { icon ->
            IconButton(
                onClick = configuration.onNavigationIonClick ?: {},
                enabled = configuration.onNavigationIonClick != null,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Navigation",
                )
            }
        }
    }

    val actionsContent: @Composable RowScope.() -> Unit = {
        configuration.actions.forEach { action ->
            IconButton(
                onClick = action.onClick,
                enabled = action.enabled,
            ) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.contentDescription,
                )
            }
        }
    }

    when (configuration.variant) {
        TopAppBarVariant.Small -> TopAppBar(
            title = titleContent,
            modifier = finalModifier,
            navigationIcon = navigationIconContent,
            actions = actionsContent,
            windowInsets = configuration.windowInsets ?: TopAppBarDefaults.windowInsets,
            colors = configuration.colors ?: TopAppBarDefaults.topAppBarColors(),
            scrollBehavior = configuration.scrollBehavior,
        )

        TopAppBarVariant.CenterAligned -> CenterAlignedTopAppBar(
            title = titleContent,
            modifier = finalModifier,
            navigationIcon = navigationIconContent,
            actions = actionsContent,
            windowInsets = configuration.windowInsets ?: TopAppBarDefaults.windowInsets,
            colors = configuration.colors ?: TopAppBarDefaults.centerAlignedTopAppBarColors(),
            scrollBehavior = configuration.scrollBehavior,
        )

        TopAppBarVariant.Medium -> MediumTopAppBar(
            title = titleContent,
            modifier = finalModifier,
            navigationIcon = navigationIconContent,
            actions = actionsContent,
            windowInsets = configuration.windowInsets ?: TopAppBarDefaults.windowInsets,
            colors = configuration.colors ?: TopAppBarDefaults.mediumTopAppBarColors(),
            scrollBehavior = configuration.scrollBehavior,
        )

        TopAppBarVariant.Large -> LargeTopAppBar(
            title = titleContent,
            modifier = finalModifier,
            navigationIcon = navigationIconContent,
            actions = actionsContent,
            windowInsets = configuration.windowInsets ?: TopAppBarDefaults.windowInsets,
            colors = configuration.colors ?: TopAppBarDefaults.largeTopAppBarColors(),
            scrollBehavior = configuration.scrollBehavior,
        )
    }
}

@Composable
fun OiidTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    variant: TopAppBarVariant = TopAppBarVariant.Small,
) {
    OiidTopAppBar(
        OiidTopAppBarConfiguration(
            title = title,
            modifier = modifier,
            variant = variant,
        ),
    )
}

@Composable
fun OiidTopAppBar(
    title: String,
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    variant: TopAppBarVariant = TopAppBarVariant.Small,
) {
    OiidTopAppBar(
        OiidTopAppBarConfiguration(
            title = title,
            modifier = modifier,
            variant = variant,
            navigationIcon = navigationIcon,
            onNavigationIonClick = onNavigationIconClick,
        ),
    )
}

@Composable
fun OiidCenterAlignedTopAppBar(
    title: String,
    onNavigationIconClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) = onNavigationIconClick?.let {
    OiidTopAppBar(
        title = title,
        onNavigationIconClick = it,
        modifier = modifier,
        variant = TopAppBarVariant.CenterAligned,
    )
} ?: OiidTopAppBar(title, modifier, TopAppBarVariant.CenterAligned)
