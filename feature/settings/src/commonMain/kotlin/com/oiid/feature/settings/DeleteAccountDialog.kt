package com.oiid.feature.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import oiid.core.base.designsystem.theme.OiidTheme

@Composable
fun DeleteAccountDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Delete account?",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Text(
                text = "Are you sure you want to delete your account? This action is permanent, and all your content will be lost, including any purchases you've made with this account.",
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                modifier = Modifier.padding(horizontal = 8.dp),
            ) {
                Text(
                    text = "Delete account",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.padding(horizontal = 8.dp),
            ) {
                Text(
                    color = OiidTheme.colorScheme.onSurface,
                    text = "Cancel",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        },
    )
}
