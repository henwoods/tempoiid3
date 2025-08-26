package com.oiid.core

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.painter.Painter

val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("SnackbarHostState not provided")
}

@Composable
expect fun oiidPainterResource(name: String): Painter

@Composable
expect fun stringResource(name: String): String

@Composable
expect fun getPackageName(): String
