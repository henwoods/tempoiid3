package com.oiid.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import oiid.core.base.designsystem.OiidMaterialTheme
import oiid.core.base.designsystem.theme.Gradients
import oiid.core.base.designsystem.theme.OiidThemeProviderImpl
import oiid.core.base.designsystem.toOiidColorScheme
import oiid.core.base.designsystem.toOiidTypography

val mezzoLightScheme = lightColorScheme(
    primary = primaryMezzoLight,
    onPrimary = onPrimaryMezzoLight,
    primaryContainer = primaryContainerMezzoLight,
    onPrimaryContainer = onPrimaryContainerMezzoLight,
    secondary = secondaryMezzoLight,
    onSecondary = onSecondaryMezzoLight,
    secondaryContainer = secondaryContainerMezzoLight,
    onSecondaryContainer = onSecondaryContainerMezzoLight,
    tertiary = tertiaryMezzoLight,
    onTertiary = onTertiaryMezzoLight,
    tertiaryContainer = tertiaryContainerMezzoLight,
    onTertiaryContainer = onTertiaryContainerMezzoLight,
    error = errorMezzoLight,
    onError = onErrorMezzoLight,
    errorContainer = errorContainerMezzoLight,
    onErrorContainer = onErrorContainerMezzoLight,
    background = backgroundMezzoLight,
    onBackground = onBackgroundMezzoLight,
    surface = surfaceMezzoLight,
    onSurface = onSurfaceMezzoLight,
    surfaceVariant = surfaceVariantMezzoLight,
    onSurfaceVariant = onSurfaceVariantMezzoLight,
    outline = outlineMezzoLight,
    outlineVariant = outlineVariantMezzoLight,
    scrim = scrimMezzoLight,
    inverseSurface = inverseSurfaceMezzoLight,
    inverseOnSurface = inverseOnSurfaceMezzoLight,
    inversePrimary = inversePrimaryMezzoLight,
    surfaceDim = surfaceDimMezzoLight,
    surfaceBright = surfaceBrightMezzoLight,
    surfaceContainerLowest = surfaceContainerLowestMezzoLight,
    surfaceContainerLow = surfaceContainerLowMezzoLight,
    surfaceContainer = surfaceContainerMezzoLight,
    surfaceContainerHigh = surfaceContainerHighMezzoLight,
    surfaceContainerHighest = surfaceContainerHighestMezzoLight,
)

val mezzoDarkScheme = lightColorScheme(
    primary = primaryMezzoDark,
    onPrimary = onPrimaryMezzoDark,
    primaryContainer = primaryContainerMezzoDark,
    onPrimaryContainer = onPrimaryContainerMezzoDark,
    secondary = secondaryMezzoDark,
    onSecondary = onSecondaryMezzoDark,
    secondaryContainer = secondaryContainerMezzoDark,
    onSecondaryContainer = onSecondaryContainerMezzoDark,
    tertiary = tertiaryMezzoDark,
    onTertiary = onTertiaryMezzoDark,
    tertiaryContainer = tertiaryContainerMezzoDark,
    onTertiaryContainer = onTertiaryContainerMezzoDark,
    error = errorMezzoDark,
    onError = onErrorMezzoDark,
    errorContainer = errorContainerMezzoDark,
    onErrorContainer = onErrorContainerMezzoDark,
    background = backgroundMezzoDark,
    onBackground = onBackgroundMezzoDark,
    surface = surfaceMezzoDark,
    onSurface = onSurfaceMezzoDark,
    surfaceVariant = surfaceVariantMezzoDark,
    onSurfaceVariant = onSurfaceVariantMezzoDark,
    outline = outlineMezzoDark,
    outlineVariant = outlineVariantMezzoDark,
    scrim = scrimMezzoDark,
    inverseSurface = inverseSurfaceMezzoDark,
    inverseOnSurface = inverseOnSurfaceMezzoDark,
    inversePrimary = inversePrimaryMezzoDark,
    surfaceDim = surfaceDimMezzoDark,
    surfaceBright = surfaceBrightMezzoDark,
    surfaceContainerLowest = surfaceContainerLowestMezzoDark,
    surfaceContainerLow = surfaceContainerLowMezzoDark,
    surfaceContainer = surfaceContainerMezzoDark,
    surfaceContainerHigh = surfaceContainerHighMezzoDark,
    surfaceContainerHighest = surfaceContainerHighestMezzoDark,
)

val nocturnScheme = darkColorScheme(
    primary = primaryNocturneDark,
    onPrimary = onPrimaryNocturneDark,
    primaryContainer = primaryContainerNocturneDark,
    onPrimaryContainer = onPrimaryContainerNocturneDark,
    secondary = secondaryNocturneDark,
    onSecondary = onSecondaryNocturneDark,
    secondaryContainer = secondaryContainerNocturneDark,
    onSecondaryContainer = onSecondaryContainerNocturneDark,
    tertiary = tertiaryNocturneDark,
    onTertiary = onTertiaryNocturneDark,
    tertiaryContainer = tertiaryContainerNocturneDark,
    onTertiaryContainer = onTertiaryContainerNocturneDark,
    error = errorNocturneDark,
    onError = onErrorNocturneDark,
    errorContainer = errorContainerNocturneDark,
    onErrorContainer = onErrorContainerNocturneDark,
    background = backgroundNocturneDark,
    onBackground = onBackgroundNocturneDark,
    surface = surfaceNocturneDark,
    onSurface = onSurfaceNocturneDark,
    surfaceVariant = surfaceVariantNocturneDark,
    onSurfaceVariant = onSurfaceVariantNocturneDark,
    outline = outlineNocturneDark,
    outlineVariant = outlineVariantNocturneDark,
    scrim = scrimNocturneDark,
    inverseSurface = inverseSurfaceNocturneDark,
    inverseOnSurface = inverseOnSurfaceNocturneDark,
    inversePrimary = inversePrimaryNocturneDark,
    surfaceDim = surfaceDimNocturneDark,
    surfaceBright = surfaceBrightNocturneDark,
    surfaceContainerLowest = surfaceContainerLowestNocturneDark,
    surfaceContainerLow = surfaceContainerLowNocturneDark,
    surfaceContainer = surfaceContainerNocturneDark,
    surfaceContainerHigh = surfaceContainerHighNocturneDark,
    surfaceContainerHighest = surfaceContainerHighestNocturneDark,
)

@Composable
fun OiidTheme(
    oiidColorScheme: OiidColorScheme,
    darkTheme: Boolean = isSystemInDarkTheme(),
    androidTheme: Boolean = false,
    useDynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val gradients = getGradients(oiidColorScheme, darkTheme)
    val colorScheme = when {
        useDynamicColor -> platformColorScheme(darkTheme, true)
        androidTheme -> if (darkTheme) darkColorScheme() else lightColorScheme()
        else -> if (darkTheme) oiidColorScheme.dark else oiidColorScheme.light
    }.toOiidColorScheme(gradients)

    val superfanTypography = Typography().toOiidTypography(fontFamily)

    val themeProvider = OiidThemeProviderImpl(
        colors = colorScheme,
        typography = superfanTypography,
    )

    OiidMaterialTheme(
        theme = themeProvider,
        content = content,
    )
}

fun getGradients(oiidColorScheme: OiidColorScheme, darkTheme: Boolean): Gradients {
    val primaryColorsGradient = Brush.horizontalGradient(
        colors = listOf(
            oiidColorScheme.dark.secondary,
            oiidColorScheme.dark.primary,
        ),
    )
    val grayGradient = Brush.horizontalGradient(
        colors = listOf(
            oiidColorScheme.dark.primaryContainer,
            oiidColorScheme.dark.surfaceContainerLowest,
        ),
    )

    return when (oiidColorScheme) {
        OiidColorScheme.Nocturne -> {
            object : Gradients {
                override val gradient: Brush get() = primaryColorsGradient
                override val gradientNav: Brush get() = grayGradient
            }
        }

        OiidColorScheme.Mezzo -> object : Gradients {
            override val gradient: Brush get() = primaryColorsGradient
            override val gradientNav: Brush get() = primaryColorsGradient
        }

        OiidColorScheme.Forte -> object : Gradients {
            override val gradient: Brush get() = primaryColorsGradient
            override val gradientNav: Brush get() = primaryColorsGradient
        }
    }
}

@Composable
expect fun platformColorScheme(useDarkTheme: Boolean, dynamicColor: Boolean): ColorScheme
