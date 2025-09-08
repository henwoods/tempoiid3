package com.oiid.core.designsystem.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import oiid.core.base.designsystem.theme.OiidTheme.spacing

@Composable
fun FeedErrorPanel(error: String?, onRetryClick: () -> Unit) {
    error?.let { error ->
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Column(modifier = Modifier.padding(spacing.md)) {
                InfoTextPanel(
                    message = error,
                    buttons = {
                        OiidButton(
                            onClick = onRetryClick,
                            text = "Retry",
                        )
                    },
                )
            }
        }
    }
}
