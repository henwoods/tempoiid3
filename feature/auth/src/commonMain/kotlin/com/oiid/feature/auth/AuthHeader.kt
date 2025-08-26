package com.oiid.feature.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.oiid.core.oiidPainterResource
import oiid.core.base.designsystem.theme.OiidTheme

@Composable
fun AuthHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AuthHeaderText("Welcome to")

        val header = oiidPainterResource("auth_header")
        Box(modifier = Modifier.size(300.dp, 91.dp)) {
            Image(
                painter = header,
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        alpha = 1f
                        translationX = 2f
                        translationY = 2f
                    },
                colorFilter = ColorFilter.tint(Color.Black),
            )

            Image(
                painter = header,
                contentDescription = "Artist Header",
                modifier = Modifier.matchParentSize(),
            )
        }
    }
}

@Composable
fun AuthHeaderText(text: String) {
    Box {
        Text(
            text = text,
            style = OiidTheme.typography.headlineMedium.copy(
                drawStyle = Stroke(width = 2f),
            ),
            color = OiidTheme.colorScheme.primary,
        )

        Text(
            text = text,
            style = OiidTheme.typography.headlineMedium.copy(
                color = OiidTheme.colorScheme.onPrimary,

            ),
            color = OiidTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
fun ColumnScope.AuthHeaderCaption(text: String) {
    Text(
        modifier = Modifier.align(Alignment.End),
        textAlign = TextAlign.End,
        text = text,
        style = OiidTheme.typography.bodySmall.copy(
            color = OiidTheme.colorScheme.onPrimary,
        ),
    )
}
