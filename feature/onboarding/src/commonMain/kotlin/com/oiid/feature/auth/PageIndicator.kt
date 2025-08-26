package com.oiid.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import oiid.core.base.designsystem.theme.OiidTheme

@Composable
fun BoxScope.PageIndicator(pagerState: PagerState) {
    Row(
        Modifier.wrapContentHeight().fillMaxWidth().align(Alignment.BottomCenter).padding(
            bottom = OiidTheme.spacing.xl,
        ),
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) OiidTheme.colorScheme.inverseOnSurface else Color.Transparent
            PageIndicatorDot(color)
        }
    }
}

@Composable
fun PageIndicatorDot(fill: Color) {
    Box(
        modifier = Modifier.padding(OiidTheme.spacing.md).clip(CircleShape).background(fill)
            .size(OiidTheme.spacing.md).border(width = 1.dp, OiidTheme.colorScheme.inverseOnSurface, CircleShape),
    )
}
