package com.oiid.feature.player.video

import androidx.compose.runtime.Composable

@Composable
actual fun FullscreenVideoPlayer(
    id: String,
    url: String,
    onDismiss: () -> Unit,
    onPositionChange: (String, Long) -> Unit,
    getInitialPosition: (String) -> Long,
) {
}
