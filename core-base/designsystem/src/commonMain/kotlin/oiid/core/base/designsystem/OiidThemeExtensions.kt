package oiid.core.base.designsystem

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.LineHeightStyle.Trim
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import oiid.core.base.designsystem.core.OiidColorScheme
import oiid.core.base.designsystem.core.OiidElevation
import oiid.core.base.designsystem.core.OiidShapes
import oiid.core.base.designsystem.core.OiidSpacing
import oiid.core.base.designsystem.core.OiidTypography
import oiid.core.base.designsystem.theme.Gradients
import oiid.core.base.designsystem.theme.OiidColorSchemeImpl
import oiid.core.base.designsystem.theme.OiidShapesImpl
import oiid.core.base.designsystem.theme.OiidTheme
import oiid.core.base.designsystem.theme.OiidTypographyImpl

/**
 * Creates [PaddingValues] using Oiid spacing tokens with horizontal and vertical values.
 *
 * This extension function provides a convenient way to create consistent padding
 * using the design system's spacing scale.
 *
 * Example usage:
 * ```
 * Box(
 *     modifier = Modifier.padding(
 *         OiidTheme.spacing.paddingValues(
 *             horizontal = OiidTheme.spacing.lg,
 *             vertical = OiidTheme.spacing.md
 *         )
 *     )
 * )
 * ```
 *
 * @param horizontal Horizontal padding (start and end). Defaults to [md]
 * @param vertical Vertical padding (top and bottom). Defaults to [md]
 * @return [PaddingValues] configured with the specified spacing
 *
 * @see OiidSpacing
 */
@Composable
fun OiidSpacing.paddingValues(
    horizontal: Dp = md,
    vertical: Dp = md,
): PaddingValues = PaddingValues(horizontal = horizontal, vertical = vertical)

/**
 * Creates [PaddingValues] using Oiid spacing tokens with individual edge values.
 *
 * This extension function provides fine-grained control over padding for each edge
 * while maintaining consistency with the design system's spacing scale.
 *
 * Example usage:
 * ```
 * Card(
 *     modifier = Modifier.padding(
 *         OiidTheme.spacing.paddingValues(
 *             start = OiidTheme.spacing.lg,
 *             top = OiidTheme.spacing.md,
 *             end = OiidTheme.spacing.lg,
 *             bottom = OiidTheme.spacing.xl
 *         )
 *     )
 * )
 * ```
 *
 * @param start Padding for the start edge (left in LTR, right in RTL). Defaults to [md]
 * @param top Padding for the top edge. Defaults to [md]
 * @param end Padding for the end edge (right in LTR, left in RTL). Defaults to [md]
 * @param bottom Padding for the bottom edge. Defaults to [md]
 * @return [PaddingValues] configured with the specified spacing for each edge
 *
 * @see OiidSpacing
 */
@Composable
fun OiidSpacing.paddingValues(
    start: Dp = md,
    top: Dp = md,
    end: Dp = md,
    bottom: Dp = md,
): PaddingValues = PaddingValues(start = start, top = top, end = end, bottom = bottom)

@Composable
fun OiidTypography.toMaterial3Typography(fontFamily: FontFamily? = FontFamily.Default): Typography {
    return Typography(
        displayLarge = this.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = this.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = this.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = this.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = this.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = this.headlineSmall.copy(
            fontFamily = fontFamily,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Bottom,
                trim = Trim.None,
            ),
        ),
        titleLarge = this.titleLarge.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Bottom,
                trim = Trim.LastLineBottom,
            ),
        ),
        titleMedium = this.titleMedium.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        ),
        titleSmall = this.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = this.bodyLarge.copy(
            fontFamily = fontFamily,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = Trim.None,
            ),
        ),
        bodyMedium = this.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = this.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = this.labelLarge.copy(
            fontFamily = fontFamily,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = Trim.LastLineBottom,
            ),
        ),
        labelMedium = this.labelMedium.copy(
            fontFamily = fontFamily,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = Trim.LastLineBottom,
            ),
        ),
        labelSmall = this.labelSmall.copy(
            fontFamily = fontFamily,
            fontSize = 10.sp,
            lineHeight = 14.sp,
            letterSpacing = 0.sp,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = Trim.LastLineBottom,
            ),
        ),
    )
}

fun Typography.toOiidTypography(fontFamily: FontFamily? = FontFamily.Default): OiidTypography =
    OiidTypographyImpl(
        displayLarge = this.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = this.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = this.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = this.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = this.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = this.headlineSmall.copy(
            fontFamily = fontFamily,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Bottom,
                trim = Trim.None,
            ),
        ),
        titleLarge = this.titleLarge.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Bottom,
                trim = Trim.LastLineBottom,
            ),
        ),
        titleMedium = this.titleMedium.copy(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        ),
        titleSmall = this.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = this.bodyLarge.copy(
            fontFamily = fontFamily,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = Trim.None,
            ),
        ),
        bodyMedium = this.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = this.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = this.labelLarge.copy(
            fontFamily = fontFamily,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = Trim.LastLineBottom,
            ),
        ),
        labelMedium = this.labelMedium.copy(
            fontFamily = fontFamily,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = Trim.LastLineBottom,
            ),
        ),
        labelSmall = this.labelSmall.copy(
            fontFamily = fontFamily,
            fontSize = 10.sp,
            lineHeight = 14.sp,
            letterSpacing = 0.sp,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = Trim.LastLineBottom,
            ),
        ),
    )

/**
 * Extension function to convert OiidTypography to Material3 Typography
 * This ensures that all Material3 components automatically use OiidTheme typography
 */
fun OiidTypography.toMaterial3Typography(): Typography {
    return Typography(
        displayLarge = this.displayLarge,
        displayMedium = this.displayMedium,
        displaySmall = this.displaySmall,
        headlineLarge = this.headlineLarge,
        headlineMedium = this.headlineMedium,
        headlineSmall = this.headlineSmall,
        titleLarge = this.titleLarge,
        titleMedium = this.titleMedium,
        titleSmall = this.titleSmall,
        bodyLarge = this.bodyLarge,
        bodyMedium = this.bodyMedium,
        bodySmall = this.bodySmall,
        labelLarge = this.labelLarge,
        labelMedium = this.labelMedium,
        labelSmall = this.labelSmall,
    )
}

fun Typography.toOiidTypography(): OiidTypography = OiidTypographyImpl(
    displayLarge = this.displayLarge,
    displayMedium = this.displayMedium,
    displaySmall = this.displaySmall,
    headlineLarge = this.headlineLarge,
    headlineMedium = this.headlineMedium,
    headlineSmall = this.headlineSmall,
    titleLarge = this.titleLarge,
    titleMedium = this.titleMedium,
    titleSmall = this.titleSmall,
    bodyLarge = this.bodyLarge,
    bodyMedium = this.bodyMedium,
    bodySmall = this.bodySmall,
    labelLarge = this.labelLarge,
    labelMedium = this.labelMedium,
    labelSmall = this.labelSmall,
)

/**
 * Extension function to convert OiidColorScheme to Material3 ColorScheme
 * This ensures that all Material3 components automatically use OiidTheme colors
 */
@Composable
fun OiidColorScheme.toMaterial3ColorScheme(): ColorScheme {
    return ColorScheme(
        primary = this.primary,
        onPrimary = this.onPrimary,
        primaryContainer = this.primaryContainer,
        onPrimaryContainer = this.onPrimaryContainer,
        inversePrimary = this.inversePrimary,
        secondary = this.secondary,
        onSecondary = this.onSecondary,
        secondaryContainer = this.secondaryContainer,
        onSecondaryContainer = this.onSecondaryContainer,
        tertiary = this.tertiary,
        onTertiary = this.onTertiary,
        tertiaryContainer = this.tertiaryContainer,
        onTertiaryContainer = this.onTertiaryContainer,
        background = this.background,
        onBackground = this.onBackground,
        surface = this.surface,
        onSurface = this.onSurface,
        surfaceVariant = this.surfaceVariant,
        onSurfaceVariant = this.onSurfaceVariant,
        surfaceTint = this.primary,
        inverseSurface = this.inverseSurface,
        inverseOnSurface = this.inverseOnSurface,
        error = this.error,
        onError = this.onError,
        errorContainer = this.errorContainer,
        onErrorContainer = this.onErrorContainer,
        outline = this.outline,
        outlineVariant = this.outlineVariant,
        scrim = this.scrim,
        surfaceBright = this.surfaceBright,
        surfaceDim = this.surfaceDim,
        surfaceContainer = this.surfaceContainer,
        surfaceContainerHigh = this.surfaceContainerHigh,
        surfaceContainerHighest = this.surfaceContainerHighest,
        surfaceContainerLow = this.surfaceContainerLow,
        surfaceContainerLowest = this.surfaceContainerLowest,
    )
}

fun ColorScheme.toOiidColorScheme(gradients: Gradients): OiidColorScheme =
    OiidColorSchemeImpl(
        primary = this.primary,
        onPrimary = this.onPrimary,
        primaryContainer = this.primaryContainer,
        onPrimaryContainer = this.onPrimaryContainer,
        inversePrimary = this.inversePrimary,
        secondary = this.secondary,
        onSecondary = this.onSecondary,
        secondaryContainer = this.secondaryContainer,
        onSecondaryContainer = this.onSecondaryContainer,
        tertiary = this.tertiary,
        onTertiary = this.onTertiary,
        tertiaryContainer = this.tertiaryContainer,
        onTertiaryContainer = this.onTertiaryContainer,
        background = this.background,
        onBackground = this.onBackground,
        surface = this.surface,
        onSurface = this.onSurface,
        surfaceVariant = this.surfaceVariant,
        onSurfaceVariant = this.onSurfaceVariant,
        surfaceTint = this.surfaceTint,
        inverseSurface = this.inverseSurface,
        inverseOnSurface = this.inverseOnSurface,
        error = this.error,
        onError = this.onError,
        errorContainer = this.errorContainer,
        onErrorContainer = this.onErrorContainer,
        outline = this.outline,
        outlineVariant = this.outlineVariant,
        scrim = this.scrim,
        surfaceBright = this.surfaceBright,
        surfaceDim = this.surfaceDim,
        surfaceContainer = this.surfaceContainer,
        surfaceContainerHigh = this.surfaceContainerHigh,
        surfaceContainerHighest = this.surfaceContainerHighest,
        surfaceContainerLow = this.surfaceContainerLow,
        surfaceContainerLowest = this.surfaceContainerLowest,
        gradients = gradients,
    )

/**
 * Extension function to convert OiidShapes to Material3 Shapes
 * This ensures that all Material3 components automatically use OiidTheme shapes
 */
fun OiidShapes.toMaterial3Shapes(): Shapes {
    return Shapes(
        extraSmall = this.extraSmall,
        small = this.small,
        medium = this.medium,
        large = this.large,
        extraLarge = this.extraLarge,
    )
}

fun Shapes.toOiidShapes(): OiidShapes = OiidShapesImpl(
    extraSmall = this.extraSmall,
    small = this.small,
    medium = this.medium,
    large = this.large,
    extraLarge = this.extraLarge,
)

/**
 * Get CardDefaults.cardElevation using OiidTheme elevation
 */
@Composable
fun OiidElevation.cardElevation(
    defaultElevation: Dp = level1,
    pressedElevation: Dp = level2,
    focusedElevation: Dp = level2,
    hoveredElevation: Dp = level2,
    draggedElevation: Dp = level4,
    disabledElevation: Dp = level0,
): CardElevation = CardDefaults.cardElevation(
    defaultElevation = defaultElevation,
    pressedElevation = pressedElevation,
    focusedElevation = focusedElevation,
    hoveredElevation = hoveredElevation,
    draggedElevation = draggedElevation,
    disabledElevation = disabledElevation,
)

/**
 * Predefined spacing combinations for common UI patterns.
 *
 * This object provides convenient access to commonly used padding configurations
 * that follow design system best practices. Use these instead of hardcoded values
 * to maintain consistency across the application.
 *
 * Example usage:
 * ```
 * // Apply standard screen padding
 * Column(
 *     modifier = Modifier.padding(OiidSpacingDefaults.screenPadding())
 * ) {
 *     // Screen content
 * }
 *
 * // Apply card content padding
 * Card {
 *     Column(
 *         modifier = Modifier.padding(OiidSpacingDefaults.cardPadding())
 *     ) {
 *         // Card content
 *     }
 * }
 * ```
 */
object OiidSpacingDefaults {
    /**
     * Standard padding for screen-level content.
     * Horizontal: lg (24dp), Vertical: md (16dp)
     * Best for: Main screen content, page layouts
     */
    @Composable
    fun screenPadding() = OiidTheme.spacing.paddingValues(
        horizontal = OiidTheme.spacing.lg,
        vertical = OiidTheme.spacing.md,
    )

    /**
     * Standard padding for card content.
     * Horizontal: md (16dp), Vertical: sm (8dp)
     * Best for: Content inside cards, list items
     */
    @Composable
    fun cardPadding() = OiidTheme.spacing.paddingValues(
        horizontal = OiidTheme.spacing.md,
        vertical = OiidTheme.spacing.sm,
    )

    /**
     * Standard padding for button content.
     * Horizontal: lg (24dp), Vertical: sm (8dp)
     * Best for: Button internal padding, touch targets
     */
    @Composable
    fun buttonPadding() = OiidTheme.spacing.paddingValues(
        horizontal = OiidTheme.spacing.lg,
        vertical = OiidTheme.spacing.sm,
    )
}

/**
 * Predefined elevation configurations for common UI patterns.
 *
 * This object provides semantically meaningful elevation presets that follow
 * Material Design elevation guidelines. Use these to maintain consistent
 * visual hierarchy throughout the application.
 *
 * Example usage:
 * ```
 * // Standard card elevation
 * Card(elevation = OiidElevationDefaults.card()) {
 *     // Card content
 * }
 *
 * // Prominent card for important content
 * Card(elevation = OiidElevationDefaults.raisedCard()) {
 *     // Important content
 * }
 * ```
 */
object OiidElevationDefaults {
    /**
     * Standard card elevation for normal content.
     * Default: level1 (1dp), Pressed: level2 (3dp)
     * Best for: Regular cards, list items, content containers
     */
    @Composable
    fun card() = OiidTheme.elevation.cardElevation(
        defaultElevation = OiidTheme.elevation.level1,
        pressedElevation = OiidTheme.elevation.level2,
    )

    /**
     * Elevated card for prominent content.
     * Default: level3 (6dp), Pressed: level4 (8dp)
     * Best for: Featured content, important cards, floating panels
     */
    @Composable
    fun raisedCard() = OiidTheme.elevation.cardElevation(
        defaultElevation = OiidTheme.elevation.level3,
        pressedElevation = OiidTheme.elevation.level4,
    )

    /**
     * High elevation for modal content.
     * Default: level5 (12dp)
     * Best for: Dialogs, modal bottom sheets, overlays
     */
    @Composable
    fun dialogCard() = OiidTheme.elevation.cardElevation(
        defaultElevation = OiidTheme.elevation.level5,
    )
}

/**
 * Provides convenient access to container color combinations.
 *
 * This extension property groups related container colors and their corresponding
 * content colors for easy access. Container colors are typically used for
 * backgrounds, surfaces, and filled components.
 *
 * Example usage:
 * ```
 * val colors = OiidTheme.colorScheme.containerColors
 *
 * Card(
 *     colors = CardDefaults.cardColors(
 *         containerColor = colors.primary,
 *         contentColor = colors.onPrimary
 *     )
 * ) {
 *     // Card content
 * }
 * ```
 *
 * @see ContainerColors
 * @see OiidColorScheme
 */
@get:Composable
val OiidColorScheme.containerColors: ContainerColors
    get() = ContainerColors(
        primary = primaryContainer,
        onPrimary = onPrimaryContainer,
        secondary = secondaryContainer,
        onSecondary = onSecondaryContainer,
        tertiary = tertiaryContainer,
        onTertiary = onTertiaryContainer,
        error = errorContainer,
        onError = onErrorContainer,
    )

/**
 * A collection of container colors and their corresponding content colors.
 *
 * This data class groups semantically related color pairs to make it easier
 * to apply consistent color schemes to components that need both background
 * and foreground colors.
 *
 * @param primary Primary container background color
 * @param onPrimary Color for content on primary container backgrounds
 * @param secondary Secondary container background color
 * @param onSecondary Color for content on secondary container backgrounds
 * @param tertiary Tertiary container background color
 * @param onTertiary Color for content on tertiary container backgrounds
 * @param error Error container background color
 * @param onError Color for content on error container backgrounds
 *
 * @see OiidColorScheme.containerColors
 */
data class ContainerColors(
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val error: Color,
    val onError: Color,
)
