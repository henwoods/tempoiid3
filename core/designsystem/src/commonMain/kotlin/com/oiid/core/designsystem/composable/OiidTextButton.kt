package com.oiid.core.designsystem.composable

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OiidTextButton(modifier: Modifier = Modifier, text: String, enabled: Boolean = true, onClick: () -> Unit) {
    Button(modifier = modifier, enabled = enabled, onClick = onClick) {
        Text(text)
    }
}
