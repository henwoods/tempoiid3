package com.oiid.feature.auth

import androidx.compose.runtime.Composable
import com.oiid.core.designsystem.composable.CircularProgress
import com.oiid.core.designsystem.composable.InfoTextPanel

@Composable
fun authProgressPanel(uiState: AuthUiState): @Composable (() -> Unit)? {
    return when (uiState) {
        is AuthUiState.Loading -> {
            if (uiState.state == AuthLoadingState.ResettingPassword || uiState.state == AuthLoadingState.SigningUp) {
                { CircularProgress() }
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
