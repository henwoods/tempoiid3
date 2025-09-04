package com.oiid.feature.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oiid.core.designsystem.composable.CircularProgress
import com.oiid.core.designsystem.composable.CircularProgressSize
import com.oiid.core.designsystem.composable.InfoTextPanel
import oiid.core.base.designsystem.theme.OiidTheme.spacing

@Composable
fun authProgressPanel(uiState: AuthUiState): @Composable (() -> Unit)? {
    return when (uiState) {
        is AuthUiState.Loading -> {
            if (uiState.state == AuthLoadingState.ResettingPassword || uiState.state == AuthLoadingState.SigningUp) {
                { CircularProgress(modifier = Modifier.padding(spacing.sm), size = CircularProgressSize.Screen) }
            } else {
                null
            }
        }

        is AuthUiState.Error -> {
            {
                InfoTextPanel(uiState.message)
            }
        }

        else -> null
    }
}
