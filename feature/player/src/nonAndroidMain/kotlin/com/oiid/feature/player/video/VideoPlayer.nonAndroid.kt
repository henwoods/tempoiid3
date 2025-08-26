package com.oiid.feature.player.video

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.model.VideoPlayerConfig
import chaintech.videoplayer.ui.video.VideoPlayerComposable
import oiid.core.base.designsystem.theme.OiidTheme

@Composable
actual fun VideoPlayer(
    modifier: Modifier,
    videoUrl: String,
    isPlaying: Boolean,
    initialPosition: Long,
    onPositionChange: ((position: Long) -> Unit)?,
    onEnterFullscreen: ((String?) -> Unit)?,
) {
    val host = MediaPlayerHost(
        mediaUrl = videoUrl,
        isPaused = true,
    )

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            host.play()
        } else {
            host.pause()
        }
    }

    val inverseSurface = OiidTheme.colorScheme.inverseSurface

    val playerConfig = remember {
        VideoPlayerConfig(
            isPauseResumeEnabled = true,
            isSeekBarVisible = true,
            isDurationVisible = false,
            seekBarThumbColor = Color.LightGray,
            isScreenResizeEnabled = false,
            seekBarActiveTrackColor = Color.LightGray,
            seekBarInactiveTrackColor = inverseSurface,
            durationTextColor = inverseSurface,
            seekBarBottomPadding = 10.dp,
            pauseResumeIconSize = 40.dp,
            isMuteControlEnabled = false,
            isZoomEnabled = false,
            isFullScreenEnabled = false,
            isAutoHideControlEnabled = false,
            isSpeedControlEnabled = false,
            isGestureVolumeControlEnabled = false,
            controlHideIntervalSeconds = 5,
            isFastForwardBackwardEnabled = false,
        )
    }

    VideoPlayerComposable(
        modifier = modifier,
        playerHost = host,
        playerConfig = playerConfig,
    )
}
