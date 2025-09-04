package com.oiid.core.designsystem.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.base.designsystem.theme.OiidTheme.spacing
import oiid.core.base.designsystem.theme.OiidTheme.typography

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmButtonText: String,
    dismissButtonText: String? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = title,
                    style = typography.titleLarge,
                    color = colorScheme.onSurface,
                )
            },
            text = {
                Text(
                    text = message,
                    style = typography.bodyLarge,
                    color = colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = spacing.sm),
                )
            },
            confirmButton = {
                OiidButton(
                    text = confirmButtonText,
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                )
            },
            dismissButton = if (dismissButtonText != null) {
                {
                    OiidTextButton(text = dismissButtonText, onClick = onDismiss, textColor = colorScheme.onSurface)
                }
            } else {
                null
            },
            containerColor = colorScheme.surface,
            shape = RoundedCornerShape(spacing.md),
            modifier = modifier,
        )
    }
}
