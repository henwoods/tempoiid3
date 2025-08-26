package com.oiid.feature.player.video

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.widget.TextView
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import androidx.media3.ui.R

@OptIn(UnstableApi::class)
@Composable
fun FullscreenPlayerDialog(
    exoPlayer: Player,
    onDismiss: () -> Unit,
) {
    val view = LocalView.current
    val window = (view.context as? Activity)?.window
    DisposableEffect(window, view) {
        if (window != null) {
            val insetsController = WindowCompat.getInsetsController(window, view)

            insetsController.hide(WindowInsetsCompat.Type.systemBars())
            insetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),
    ) {
        BackHandler(onBack = onDismiss)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
        ) {
            AndroidView(
                factory = {
                    PlayerView(it).apply {
                        this.player = exoPlayer
                        useController = true
                        setFullscreenButtonClickListener { onDismiss() }
                        player = exoPlayer

                        useController = true
                        controllerShowTimeoutMs = 1000

                        setShowNextButton(false)
                        setShowPreviousButton(false)
                        setShowShuffleButton(false)
                        setShowSubtitleButton(false)
                        setShowVrButton(false)
                        setRepeatToggleModes(Player.REPEAT_MODE_OFF)

                        post {
                            val durationView = findViewById<TextView>(R.id.exo_duration)
                            durationView?.visibility = View.GONE

                            val positionView = findViewById<TextView>(R.id.exo_position)
                            positionView?.visibility = View.GONE
                        }
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
