package com.oiid.core.designsystem

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import oiid.core.base.designsystem.theme.OiidTheme

@Composable
fun diagonalCornerShape(size: Dp = OiidTheme.spacing.xl): RoundedCornerShape {
    return RoundedCornerShape(size, 0.dp, size, 0.dp)
}
