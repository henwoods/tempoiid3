package com.oiid.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.base.designsystem.theme.OiidTheme.spacing

@Composable
fun RowScope.OiidNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
    selectedIcon: @Composable () -> Unit = icon,
    label: @Composable (() -> Unit)? = null,
) {
    NavigationBarItem(
        colors = NavigationBarItemDefaults.colors().copy(
            selectedIconColor = colorScheme.onTertiary,
            unselectedIconColor = colorScheme.onTertiary,
            selectedTextColor = colorScheme.onTertiary,
            unselectedTextColor = colorScheme.onTertiary,
            selectedIndicatorColor = Color.Transparent,

            ),
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier
            // .padding(16.dp).heightIn(max = 64.dp)
            .scale(
                if (selected) {
                    1.15f
                } else {
                    1f
                },
            ),
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
    )
}

@Composable
fun OiidNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val cornerShape = RoundedCornerShape(50)

    Surface(
        modifier = modifier.background(Color.Transparent).clip(cornerShape)
            .padding(bottom = spacing.md, start = spacing.md, end = spacing.md),
        tonalElevation = spacing.xs,
        shape = cornerShape,
    ) {
        Box(modifier = Modifier.background(brush = colorScheme.gradients.pillBackground, alpha = .75f)) {
            NavigationBar(
                modifier = modifier.padding(horizontal = spacing.sm),
                containerColor = Color.Transparent,
                tonalElevation = 0.dp,
                content = content,
            )
        }
    }
}
