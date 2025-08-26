package oiid.core.base.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import oiid.core.base.designsystem.core.OiidThemeProvider
import oiid.core.base.designsystem.theme.LocalOiidColors
import oiid.core.base.designsystem.theme.LocalOiidElevation
import oiid.core.base.designsystem.theme.LocalOiidShapes
import oiid.core.base.designsystem.theme.LocalOiidSpacing
import oiid.core.base.designsystem.theme.LocalOiidTypography
import oiid.core.base.designsystem.theme.OiidTheme
import oiid.core.base.designsystem.theme.OiidThemeProviderImpl
import oiid.core.base.designsystem.theme.oiidTheme

/**
 * OiidMaterialTheme provides Material3 integration for OiidMaterialTheme.
 * This composable applies OiidMaterialTheme values to MaterialTheme automatically,
 * making all Material3 components use OiidMaterialTheme design tokens.
 *
 * @param theme OiidThemeProvider instance containing design tokens
 * @param content The composable content that will have access to both OiidMaterialTheme and MaterialTheme
 */
@Composable
fun OiidMaterialTheme(
    theme: OiidThemeProvider = OiidThemeProviderImpl(),
    content: @Composable () -> Unit,
) {
    // Convert OiidMaterialTheme values to Material3 equivalents
    val materialColorScheme = theme.colors.toMaterial3ColorScheme()
    val materialTypography = theme.typography.toMaterial3Typography()
    val materialShapes = theme.shapes.toMaterial3Shapes()

    // Provide both OiidMaterialTheme composition locals and MaterialTheme
    CompositionLocalProvider(
        LocalOiidColors provides theme.colors,
        LocalOiidTypography provides theme.typography,
        LocalOiidShapes provides theme.shapes,
        LocalOiidSpacing provides theme.spacing,
        LocalOiidElevation provides theme.elevation,
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = materialTypography,
            shapes = materialShapes,
            content = content,
        )
    }
}

/**
 * OiidMaterialTheme with dark theme support.
 * Provides automatic light/dark theme switching with Material3 integration.
 *
 * @param darkTheme Whether to use dark theme. Defaults to system preference.
 * @param lightTheme OiidThemeProvider for light theme
 * @param darkTheme OiidThemeProvider for dark theme
 * @param content The composable content that will have access to both OiidTheme and MaterialTheme
 */
@Composable
fun OiidMaterialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    lightTheme: OiidThemeProvider = OiidThemeProviderImpl(),
    darkThemeProvider: OiidThemeProvider = OiidThemeProviderImpl(),
    content: @Composable () -> Unit,
) {
    val selectedTheme = if (darkTheme) darkThemeProvider else lightTheme
    OiidMaterialTheme(
        theme = selectedTheme,
        content = content,
    )
}

/**
 * DSL builder for creating OiidMaterialTheme with custom configuration
 */
@Composable
fun OiidMaterialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeBuilder: @Composable (Boolean) -> OiidThemeProvider,
    content: @Composable () -> Unit,
) {
    val theme = themeBuilder(darkTheme)
    OiidMaterialTheme(
        theme = theme,
        content = content,
    )
}