package oiid.core.base.designsystem.core

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import oiid.core.base.designsystem.theme.Gradients
import kotlin.reflect.KClass

interface OiidComponent {
    val testTag: String?
    val contentDescription: String?
    val modifier: Modifier
}

interface Clickable {
    val onClick: () -> Unit
    val enabled: Boolean
    val interactionSource: MutableInteractionSource?
}

interface Styleable {
    val colors: ComponentColors?
    val shape: Shape?
    val elevation: ComponentElevation?
}

interface Themeable {
    val theme: ComponentTheme?
}

interface ComponentColors

interface ComponentElevation

interface ComponentTheme

interface ThemeStrategy {
    fun applyTheme(component: OiidComponent): ComponentTheme
}

interface ComponentFactory<T : OiidComponent> {
    fun create(configuration: ComponentConfiguration): T
}

interface ComponentConfiguration {
    fun build(): OiidComponent
}

@Stable
interface ComponentState<T> {
    val value: T
    fun update(newValue: T)
}

sealed interface ComponentVariant {
    val name: String
    val isEnabled: Boolean get() = true
}

interface ComponentComposer {
    @Composable
    fun compose(components: List<OiidComponent>): Unit
}

interface Animatable {
    val animationDuration: Long
    val animationEasing: androidx.compose.animation.core.Easing?
}

interface AccessibilityProvider {
    val semantics: androidx.compose.ui.semantics.SemanticsPropertyReceiver.() -> Unit
    val contentDescription: String?
    val role: androidx.compose.ui.semantics.Role?
}

interface OiidThemeProvider {
    val colors: OiidColorScheme
    val typography: OiidTypography
    val shapes: OiidShapes
    val spacing: OiidSpacing
    val elevation: OiidElevation
}

@Stable
interface OiidColorScheme {
    val primary: Color
    val onPrimary: Color
    val primaryContainer: Color
    val onPrimaryContainer: Color
    val inversePrimary: Color
    val secondary: Color
    val onSecondary: Color
    val secondaryContainer: Color
    val onSecondaryContainer: Color
    val tertiary: Color
    val onTertiary: Color
    val tertiaryContainer: Color
    val onTertiaryContainer: Color
    val background: Color
    val onBackground: Color
    val surface: Color
    val onSurface: Color
    val surfaceVariant: Color
    val onSurfaceVariant: Color
    val surfaceTint: Color
    val inverseSurface: Color
    val inverseOnSurface: Color
    val error: Color
    val onError: Color
    val errorContainer: Color
    val onErrorContainer: Color
    val outline: Color
    val outlineVariant: Color
    val scrim: Color
    val surfaceBright: Color
    val surfaceDim: Color
    val surfaceContainer: Color
    val surfaceContainerHigh: Color
    val surfaceContainerHighest: Color
    val surfaceContainerLow: Color
    val surfaceContainerLowest: Color
    val gradients: Gradients
}

@Stable
interface OiidTypography {
    val displayLarge: androidx.compose.ui.text.TextStyle
    val displayMedium: androidx.compose.ui.text.TextStyle
    val displaySmall: androidx.compose.ui.text.TextStyle
    val headlineLarge: androidx.compose.ui.text.TextStyle
    val headlineMedium: androidx.compose.ui.text.TextStyle
    val headlineSmall: androidx.compose.ui.text.TextStyle
    val titleLarge: androidx.compose.ui.text.TextStyle
    val titleMedium: androidx.compose.ui.text.TextStyle
    val titleSmall: androidx.compose.ui.text.TextStyle
    val bodyLarge: androidx.compose.ui.text.TextStyle
    val bodyMedium: androidx.compose.ui.text.TextStyle
    val bodySmall: androidx.compose.ui.text.TextStyle
    val labelLarge: androidx.compose.ui.text.TextStyle
    val labelMedium: androidx.compose.ui.text.TextStyle
    val labelSmall: androidx.compose.ui.text.TextStyle
}

@Stable
interface OiidShapes {
    val extraSmall: CornerBasedShape
    val small: CornerBasedShape
    val medium: CornerBasedShape
    val large: CornerBasedShape
    val extraLarge: CornerBasedShape
}

@Stable
interface OiidSpacing {
    val xs: Dp
    val sm: Dp
    val md: Dp
    val lg: Dp
    val xl: Dp
    val xxl: Dp
}

@Stable
interface OiidElevation {
    val level0: Dp
    val level1: Dp
    val level2: Dp
    val level3: Dp
    val level4: Dp
    val level5: Dp
}

interface ComponentRenderer<T : OiidComponent> {
    @Composable
    fun render(component: T)
}

interface ComponentRegistry {
    fun <T : OiidComponent> register(type: KClass<T>, renderer: ComponentRenderer<T>)
    fun <T : OiidComponent> getRenderer(type: KClass<T>): ComponentRenderer<T>?
}

@DslMarker
annotation class ComponentDsl

@ComponentDsl
interface ComponentConfigurationScope {
    var testTag: String?
    var contentDescription: String?
    var enabled: Boolean
    var modifier: Modifier
}
