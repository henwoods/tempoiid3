package com.oiid.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
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

val mezzoDarkScheme = darkColorScheme(
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

val nocturneLightScheme = lightColorScheme(
    primary = primaryNocturneLight,
    onPrimary = onPrimaryNocturneLight,
    primaryContainer = primaryContainerNocturneLight,
    onPrimaryContainer = onPrimaryContainerNocturneLight,
    secondary = secondaryNocturneLight,
    onSecondary = onSecondaryNocturneLight,
    secondaryContainer = secondaryContainerNocturneLight,
    onSecondaryContainer = onSecondaryContainerNocturneLight,
    tertiary = tertiaryNocturneLight,
    onTertiary = onTertiaryNocturneLight,
    tertiaryContainer = tertiaryContainerNocturneLight,
    onTertiaryContainer = onTertiaryContainerNocturneLight,
    error = errorNocturneLight,
    onError = onErrorNocturneLight,
    errorContainer = errorContainerNocturneLight,
    onErrorContainer = onErrorContainerNocturneLight,
    background = backgroundNocturneLight,
    onBackground = onBackgroundNocturneLight,
    surface = surfaceNocturneLight,
    onSurface = onSurfaceNocturneLight,
    surfaceVariant = surfaceVariantNocturneLight,
    onSurfaceVariant = onSurfaceVariantNocturneLight,
    outline = outlineNocturneLight,
    outlineVariant = outlineVariantNocturneLight,
    scrim = scrimNocturneLight,
    inverseSurface = inverseSurfaceNocturneLight,
    inverseOnSurface = inverseOnSurfaceNocturneLight,
    inversePrimary = inversePrimaryNocturneLight,
    surfaceDim = surfaceDimNocturneLight,
    surfaceBright = surfaceBrightNocturneLight,
    surfaceContainerLowest = surfaceContainerLowestNocturneLight,
    surfaceContainerLow = surfaceContainerLowNocturneLight,
    surfaceContainer = surfaceContainerNocturneLight,
    surfaceContainerHigh = surfaceContainerHighNocturneLight,
    surfaceContainerHighest = surfaceContainerHighestNocturneLight,
)

val nocturnDarkScheme = darkColorScheme(
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


val forteLightScheme = lightColorScheme(
    primary = primaryForteLight,
    onPrimary = onPrimaryForteLight,
    primaryContainer = primaryContainerForteLight,
    onPrimaryContainer = onPrimaryContainerForteLight,
    secondary = secondaryForteLight,
    onSecondary = onSecondaryForteLight,
    secondaryContainer = secondaryContainerForteLight,
    onSecondaryContainer = onSecondaryContainerForteLight,
    tertiary = tertiaryForteLight,
    onTertiary = onTertiaryForteLight,
    tertiaryContainer = tertiaryContainerForteLight,
    onTertiaryContainer = onTertiaryContainerForteLight,
    error = errorForteLight,
    onError = onErrorForteLight,
    errorContainer = errorContainerForteLight,
    onErrorContainer = onErrorContainerForteLight,
    background = backgroundForteLight,
    onBackground = onBackgroundForteLight,
    surface = surfaceForteLight,
    onSurface = onSurfaceForteLight,
    surfaceVariant = surfaceVariantForteLight,
    onSurfaceVariant = onSurfaceVariantForteLight,
    outline = outlineForteLight,
    outlineVariant = outlineVariantForteLight,
    scrim = scrimForteLight,
    inverseSurface = inverseSurfaceForteLight,
    inverseOnSurface = inverseOnSurfaceForteLight,
    inversePrimary = inversePrimaryForteLight,
    surfaceDim = surfaceDimForteLight,
    surfaceBright = surfaceBrightForteLight,
    surfaceContainerLowest = surfaceContainerLowestForteLight,
    surfaceContainerLow = surfaceContainerLowForteLight,
    surfaceContainer = surfaceContainerForteLight,
    surfaceContainerHigh = surfaceContainerHighForteLight,
    surfaceContainerHighest = surfaceContainerHighestForteLight,
)

val forteDarkScheme = darkColorScheme(
    primary = primaryForteDark,
    onPrimary = onPrimaryForteDark,
    primaryContainer = primaryContainerForteDark,
    onPrimaryContainer = onPrimaryContainerForteDark,
    secondary = secondaryForteDark,
    onSecondary = onSecondaryForteDark,
    secondaryContainer = secondaryContainerForteDark,
    onSecondaryContainer = onSecondaryContainerForteDark,
    tertiary = tertiaryForteDark,
    onTertiary = onTertiaryForteDark,
    tertiaryContainer = tertiaryContainerForteDark,
    onTertiaryContainer = onTertiaryContainerForteDark,
    error = errorForteDark,
    onError = onErrorForteDark,
    errorContainer = errorContainerForteDark,
    onErrorContainer = onErrorContainerForteDark,
    background = backgroundForteDark,
    onBackground = onBackgroundForteDark,
    surface = surfaceForteDark,
    onSurface = onSurfaceForteDark,
    surfaceVariant = surfaceVariantForteDark,
    onSurfaceVariant = onSurfaceVariantForteDark,
    outline = outlineForteDark,
    outlineVariant = outlineVariantForteDark,
    scrim = scrimForteDark,
    inverseSurface = inverseSurfaceForteDark,
    inverseOnSurface = inverseOnSurfaceForteDark,
    inversePrimary = inversePrimaryForteDark,
    surfaceDim = surfaceDimForteDark,
    surfaceBright = surfaceBrightForteDark,
    surfaceContainerLowest = surfaceContainerLowestForteDark,
    surfaceContainerLow = surfaceContainerLowForteDark,
    surfaceContainer = surfaceContainerForteDark,
    surfaceContainerHigh = surfaceContainerHighForteDark,
    surfaceContainerHighest = surfaceContainerHighestForteDark,
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
    return when (oiidColorScheme) {
        OiidColorScheme.Nocturne -> if (darkTheme) NocturneDarkGradients else NocturneLightGradients
        OiidColorScheme.Mezzo -> if (darkTheme) MezzoDarkGradients else MezzoLightGradients
        OiidColorScheme.Forte -> if (darkTheme) ForteDarkGradients else ForteLightGradients
    }
}

@Composable
expect fun platformColorScheme(useDarkTheme: Boolean, dynamicColor: Boolean): ColorScheme
