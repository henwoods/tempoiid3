package com.oiid.feature.player.video

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

@Composable
actual fun FullscreenVideoPlayer(
    id: String,
    url: String,
    onDismiss: () -> Unit,
    onPositionChange: (videoId: String, position: Long) -> Unit,
    getInitialPosition: (videoId: String) -> Long,
) {
    val context = LocalContext.current

    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = true
        }
    }

    LaunchedEffect(id) {
        exoPlayer.setMediaItem(MediaItem.fromUri(url))
        exoPlayer.seekTo(getInitialPosition(id))
        exoPlayer.prepare()
    }

    DisposableEffect(Unit) {
        onDispose {
            onPositionChange(id, exoPlayer.currentPosition)
            exoPlayer.release()
        }
    }

    FullscreenPlayerDialog(
        exoPlayer = exoPlayer,
        onDismiss = onDismiss,
    )
}
