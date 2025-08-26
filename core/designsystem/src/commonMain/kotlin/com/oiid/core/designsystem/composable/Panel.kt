package com.oiid.core.designsystem.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.oiid.core.designsystem.ext.panelFilledBackground
import oiid.core.base.designsystem.theme.OiidTheme
import oiid.core.base.designsystem.theme.OiidTheme.spacing

@Composable
fun InfoTextPanel(
    message: String? = null,
    buttons: @Composable (() -> Unit)? = null,
    backgroundColor: Color = OiidTheme.colorScheme.surface.copy(alpha = .7f),
) {
    if (message != null) {
        Column(
            modifier = Modifier.fillMaxWidth().panelFilledBackground(backgroundColor)
                .padding(spacing.sm),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(spacing.md).fillMaxWidth(),
                color = OiidTheme.colorScheme.onSurface,
                text = message,
                textAlign = TextAlign.Center,
                style = OiidTheme.typography.bodyLarge,
            )
            buttons?.invoke()
        }
    }
}

@Composable
fun TextPanel(modifier: Modifier = Modifier, backgroundColor: Color, content: @Composable () -> Unit) {
    Column(
        modifier = modifier.panelFilledBackground(backgroundColor.copy(alpha = .7f)),
    ) {
        content()
    }
}
