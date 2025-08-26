package com.oiid.core.model.ui

sealed class UiEvent {
    data class LaunchUri(val uri: String) : UiEvent()
    data class ShowSnackbar(val message: String) : UiEvent()
}
