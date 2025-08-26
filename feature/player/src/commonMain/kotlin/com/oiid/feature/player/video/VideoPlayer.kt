package com.oiid.feature.player.video

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun VideoPlayer(
    modifier: Modifier,
    videoUrl: String,
    isPlaying: Boolean,
    initialPosition: Long,
    onPositionChange: ((position: Long) -> Unit)?,
    onEnterFullscreen: ((String?) -> Unit)? = null,
)
