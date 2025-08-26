package com.oiid.core.designsystem.ext

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Stable
fun bottomNavPadding(contentPadding: PaddingValues = PaddingValues()): PaddingValues {
    return PaddingValues(
        top = contentPadding.calculateTopPadding(),
        start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
        end = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
        bottom = 164.dp,
    )
}
