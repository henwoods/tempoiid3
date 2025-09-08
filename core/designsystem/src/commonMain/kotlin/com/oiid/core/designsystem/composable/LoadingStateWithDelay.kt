package com.oiid.core.designsystem.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay

data class LoadStateResult(
    val showProgress: Boolean = false,
    val isRefreshing: Boolean = false,
)

@Composable
fun rememberDelayedLoadState(
    isInitialLoading: Boolean,
    isRefreshing: Boolean
): State<LoadStateResult> {
    val loadState = remember { mutableStateOf(LoadStateResult()) }

    LaunchedEffect(isInitialLoading) {
        if (isInitialLoading) {
            delay(300)
            if (isInitialLoading) {
                loadState.value = LoadStateResult(showProgress = true, isRefreshing = isRefreshing)
            }
        } else {
            loadState.value = LoadStateResult(showProgress = false, isRefreshing = isRefreshing)
        }
    }

    return loadState
}