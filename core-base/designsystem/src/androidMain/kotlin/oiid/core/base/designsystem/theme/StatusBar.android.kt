package oiid.core.base.designsystem.theme

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext

/**
 * Unused class for now, we might want to use it in the future to micro manage the status bar
 * colour on some screens, but we will do that when there is time to stuff about.
 */


@Composable
actual fun ThemedStatusBar(color: Color) {
    val activity = activityFromContext()

    LaunchedEffect(color) {
        if (color == Color.Transparent) {
            activity?.statusBarAutoEdgeToEdge(color.isDark())
        } else {
            activity?.statusBarForceColor(color)
        }
    }
}

@Composable
actual fun SystemBarsEffect(isDarkTheme: Boolean) {
    val activity = activityFromContext()

    LaunchedEffect(!isDarkTheme) {
        activity?.enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                Color.Transparent.toArgb(),
            ),
            navigationBarStyle = SystemBarStyle.dark(
                Color.Transparent.toArgb(),
            ),
        )
    }
}

@Composable
private fun activityFromContext(): ComponentActivity? {
    return LocalContext.current as ComponentActivity
}

private fun ComponentActivity.statusBarForceColor(color: Color) {
    val isColorDark = color.isDark()

    enableEdgeToEdge(
        statusBarStyle = if (isColorDark) {
            SystemBarStyle.dark(color.toArgb())
        } else {
            SystemBarStyle.light(color.toArgb(), color.toArgb())
        },
    )
}

private fun ComponentActivity.statusBarAutoEdgeToEdge(isDarkTheme: Boolean) {
    if (!isDarkTheme) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb(),
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb(),
            ),
        )
    } else {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                Color.White.toArgb(),
            ),
            navigationBarStyle = SystemBarStyle.dark(
                Color.White.toArgb(),
            ),
        )
    }
}

private fun Color.isDark() = (red * 299 + green * 587 + blue * 114) / 1000 < 0.5
