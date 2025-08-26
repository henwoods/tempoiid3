package com.oiid.feature.player.video

import androidx.compose.runtime.Composable

@Composable
expect fun FullscreenVideoPlayer(
    id: String,
    url: String,
    onDismiss: () -> Unit,
    onPositionChange: (videoId: String, position: Long) -> Unit,
    getInitialPosition: (videoId: String) -> Long,
)
