package com.oiid.core.designsystem.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.oiid.core.designsystem.diagonalCornerShape
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.base.designsystem.theme.OiidTheme.spacing

@Composable
fun Tag(
    modifier: Modifier = Modifier,
    backgroundColor: Color = colorScheme.inverseSurface,
    contentPadding: PaddingValues = PaddingValues(horizontal = spacing.sm),
    content: @Composable () -> Unit,
) {
    TagContainer(
        modifier = modifier.background(
            color = backgroundColor,
            shape = diagonalCornerShape(size = spacing.xs),
        ),
        contentPadding = contentPadding,
        content = content,
    )
}

@Composable
fun Tag(
    modifier: Modifier = Modifier,
    backgroundBrush: Brush,
    contentPadding: PaddingValues = PaddingValues(horizontal = spacing.xs),
    content: @Composable () -> Unit,
) {
    TagContainer(
        modifier = modifier.background(
            brush = backgroundBrush,
            shape = diagonalCornerShape(size = spacing.xs),
        ),
        contentPadding = contentPadding,
        content = content,
    )
}

@Composable
private fun TagContainer(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.padding(contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
fun TagTextLabel(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 12.sp,
    color: Color = colorScheme.inverseOnSurface,
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = fontSize,
        color = color,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun TagIconLabel(
    modifier: Modifier = Modifier,
    size: Dp = spacing.md,
    imageVector: ImageVector,
    contentDescription: String,
    tint: Color = colorScheme.onTertiary,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier.size(size),
        tint = tint,
    )
}
