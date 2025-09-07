package com.oiid.core.designsystem

import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val NAV_BAR_HEIGHT = 84.dp
val TOP_BAR_HEIGHT = 56.dp

fun appBarHeight(): Dp {
    return TOP_BAR_HEIGHT
}

fun Modifier.navBarHeight(): Modifier {
    return height(NAV_BAR_HEIGHT)
}
