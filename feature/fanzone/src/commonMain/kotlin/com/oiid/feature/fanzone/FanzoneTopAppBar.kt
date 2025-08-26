package com.oiid.feature.fanzone

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import oiid.core.base.designsystem.component.OiidTopAppBar
import oiid.core.base.designsystem.core.OiidTopAppBarConfiguration
import oiid.core.base.designsystem.core.TopAppBarAction
import oiid.core.base.designsystem.core.TopAppBarVariant
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FanzoneScreenAppBar(onAddPostClick: () -> Unit) {
    OiidTopAppBar(
        configuration = OiidTopAppBarConfiguration(
            title = "FAN-ZONE",
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
                .copy(containerColor = colorScheme.background),
            actions = listOf(
                TopAppBarAction(
                    Icons.Default.EditNote, "Add Post",
                    onClick = onAddPostClick,
                ),
            ),
            variant = TopAppBarVariant.CenterAligned,
        ),
    )
}