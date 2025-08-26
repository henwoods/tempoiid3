package com.oiid.core.designsystem.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import oiid.core.base.designsystem.theme.OiidTheme

@Composable
fun AuthStack(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(OiidTheme.spacing.sm),
    ) {
        content()
    }
}

@Composable
fun AuthContainer(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    title: @Composable () -> Unit,
    fields: @Composable (() -> Unit)? = null,
    buttons: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(paddingValues).navigationBarsPadding().padding(
            start = OiidTheme.spacing.xl,
            end = OiidTheme.spacing.xl,
            top = OiidTheme.spacing.md,
            bottom = OiidTheme.spacing.md,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        title()
        fields?.invoke()
        buttons()
    }
}
