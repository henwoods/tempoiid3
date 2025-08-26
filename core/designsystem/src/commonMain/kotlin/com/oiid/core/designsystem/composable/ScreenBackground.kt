package com.oiid.core.designsystem.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.ImageLoader
import coil3.compose.AsyncImage
import oiid.core.base.designsystem.AppStateViewModel
import oiid.core.base.designsystem.FullScreenBackground
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ScreenWithBackground(
    key: String,
    background: FullScreenBackground,
    appStateViewModel: AppStateViewModel = koinViewModel(),
    content: @Composable () -> Unit,
) {
    val instance = remember { Any() }

    DisposableEffect(key, instance) {
        appStateViewModel.setBackground(background, key, instance)

        onDispose {
            appStateViewModel.clearBackground(key, instance)
        }
    }

    content()
}

@Composable
fun ScreenBackground(
    imageLoader: ImageLoader,
    background: FullScreenBackground,
    modifier: Modifier = Modifier,
) {
    // If a crossfade is add here at a later date it will need some logic to avoid flickering the background on the
    // auth screens
    when (background) {
        is FullScreenBackground.Resource -> {
            AsyncImage(
                model = background.res,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier.fillMaxSize(),
                imageLoader = imageLoader,
            )
        }

        is FullScreenBackground.Url -> {
            AsyncImage(
                imageLoader = imageLoader,
                model = background.url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier.fillMaxSize(),
            )
        }

        FullScreenBackground.None -> {
        }

        is FullScreenBackground.Paint -> {
            Image(
                painter = background.painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier.fillMaxSize(),
            )
        }
    }
}
