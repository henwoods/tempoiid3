package com.oiid.core.designsystem.ext

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import oiid.core.base.designsystem.theme.OiidTheme

@Composable
fun Modifier.panelFilledBackground(color: Color): Modifier {
    return clip(shape = RoundedCornerShape(size = OiidTheme.spacing.md)).background(
        color = color,
    )
}
