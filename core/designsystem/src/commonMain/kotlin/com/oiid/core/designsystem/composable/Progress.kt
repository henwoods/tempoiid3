package com.oiid.core.designsystem.composable

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import oiid.core.base.designsystem.theme.OiidTheme

@Composable
fun CircularProgress() {
    Box(
        modifier = Modifier.padding(vertical = OiidTheme.spacing.md).fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun LinearProgress(modifier: Modifier = Modifier) {
    LinearProgressIndicator(
        modifier = modifier.fillMaxWidth(),
        color = OiidTheme.colorScheme.primary,
        trackColor = OiidTheme.colorScheme.background,
    )
}

@Composable
fun FeedProgress(text: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(OiidTheme.spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = text,
            style = OiidTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        FiveLinesProgress(size = 192.dp)
    }
}

@Composable
fun FiveLinesProgress(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    size: Dp = 50.dp,
    speed: Double = 0.5,
) {
    val transition = rememberInfiniteTransition(label = "loading_lines_transition")
    val animationDurationMillis = (speed * 1000).toInt()

    val delays = listOf(
        (animationDurationMillis / 3 * 2),
        (animationDurationMillis / 3),
        0,
        (animationDurationMillis / 3),
        (animationDurationMillis / 3 * 2),
    )

    val animatedHeights = delays.map { delay ->
        transition.animateFloat(
            initialValue = 0.8f,
            targetValue = 0.3f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = animationDurationMillis,
                    delayMillis = delay,
                    easing = FastOutSlowInEasing,
                ),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "line_height_animation",
        )
    }

    Row(
        modifier = modifier.size(size),
        horizontalArrangement = Arrangement.spacedBy(size / 15, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        animatedHeights.forEach { heightFraction ->
            Box(
                modifier = Modifier
                    .width(size / 10)
                    .height(size * heightFraction.value)
                    .clip(RoundedCornerShape(25.dp))
                    .background(color),
            )
        }
    }
}
