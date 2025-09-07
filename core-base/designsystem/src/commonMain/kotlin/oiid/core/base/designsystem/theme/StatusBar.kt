package oiid.core.base.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * We can use this to change the color of the status bar to something specific (e.g. on post detail screen)
 */
@Composable
expect fun ThemedStatusBar(color: Color)

@Composable
expect fun SystemBarsEffect(isDarkTheme: Boolean)