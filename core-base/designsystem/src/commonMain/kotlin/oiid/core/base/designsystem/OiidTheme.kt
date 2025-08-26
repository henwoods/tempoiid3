package oiid.core.base.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import oiid.core.base.designsystem.core.OiidThemeProvider
import oiid.core.base.designsystem.theme.LocalOiidColors
import oiid.core.base.designsystem.theme.LocalOiidElevation
import oiid.core.base.designsystem.theme.LocalOiidShapes
import oiid.core.base.designsystem.theme.LocalOiidSpacing
import oiid.core.base.designsystem.theme.LocalOiidTypography
import oiid.core.base.designsystem.theme.OiidThemeProviderImpl

/**
 * Oiid provides the core theming composable that makes all Oiid design tokens
 * available to child components through Composition Locals.
 *
 * This composable sets up the theme hierarchy by providing:
 * - [LocalOiidColors] for color design tokens
 * - [LocalOiidTypography] for typography design tokens
 * - [LocalOiidShapes] for shape design tokens
 * - [LocalOiidSpacing] for spacing design tokens
 * - [LocalOiidElevation] for elevation design tokens
 *
 * Usage:
 * ```
 * Oiid {
 *     // All child components can now access:
 *     // Oiid.colorScheme
 *     // Oiid.typography
 *     // Oiid.shapes
 *     // Oiid.spacing
 *     // Oiid.elevation
 *     MyScreen()
 * }
 * ```
 *
 * For Material3 integration, consider using [OiidMaterialTheme] instead,
 * which provides both Oiid design tokens and Material3 theme compatibility.
 *
 * @param theme The theme provider containing all design tokens. Defaults to [OiidThemeProviderImpl]
 * @param content The composable content that will have access to the theme
 *
 * @see OiidMaterialTheme
 * @see OiidThemeProvider
 */
@Composable
fun OiidTheme(
    theme: OiidThemeProvider = OiidThemeProviderImpl(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalOiidColors provides theme.colors,
        LocalOiidTypography provides theme.typography,
        LocalOiidShapes provides theme.shapes,
        LocalOiidSpacing provides theme.spacing,
        LocalOiidElevation provides theme.elevation,
    ) {
        content()
    }
}
