package com.oiid.core.designsystem.theme

import androidx.compose.material3.ColorScheme

enum class OiidColorScheme(val dark: ColorScheme, val light: ColorScheme) {
    Nocturne(nocturnDarkScheme, nocturneLightScheme), Mezzo(mezzoDarkScheme, mezzoLightScheme), Forte(
        forteDarkScheme,
        forteLightScheme,
    ),
}
