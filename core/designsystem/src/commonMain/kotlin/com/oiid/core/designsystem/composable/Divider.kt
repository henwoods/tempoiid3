package com.oiid.core.designsystem.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import oiid.core.base.designsystem.theme.OiidTheme

@Composable
fun DashedVerticalDivider(
    modifier: Modifier = Modifier
        .fillMaxHeight()
        .width(2.dp),
    color: Color = OiidTheme.colorScheme.onSurface,
    dashLength: Float = 6f,
    gapLength: Float = 6f,
) {
    Canvas(modifier = modifier) {
        drawLine(
            color = color,
            start = Offset(x = size.width / 2, y = 0f),
            end = Offset(x = size.width / 2, y = size.height),
            strokeWidth = size.width,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLength, gapLength), 0f),
        )
    }
}
