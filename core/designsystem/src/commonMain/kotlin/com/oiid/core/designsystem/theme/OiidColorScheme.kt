package com.oiid.core.designsystem.theme

import androidx.compose.material3.ColorScheme

enum class OiidColorScheme(val dark: ColorScheme, val light: ColorScheme) {
    Nocturne(nocturnScheme, nocturnScheme),
    Mezzo(mezzoDarkScheme, mezzoLightScheme),
    Forte(
        nocturnScheme,
        nocturnScheme,
    ),
}
