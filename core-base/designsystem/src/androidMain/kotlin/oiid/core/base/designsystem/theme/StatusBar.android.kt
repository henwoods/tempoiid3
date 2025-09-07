package oiid.core.base.designsystem.theme

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun ThemedStatusBar(color: Color) {
    val activity = activityFromContext()

    LaunchedEffect(color) {
        if (color == Color.Transparent) {
            activity?.statusBarAutoEdgeToEdge()
        } else {
            activity?.statusBarForceColor(color)
        }
    }
}

@Composable
actual fun SystemBarsEffect(isDarkTheme: Boolean) {
    val activity = activityFromContext()

    LaunchedEffect(isDarkTheme) {
        activity?.statusBarAutoEdgeToEdge()
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

private fun ComponentActivity.statusBarAutoEdgeToEdge() {
    enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.auto(
            lightScrim = Color.Transparent.toArgb(),
            darkScrim = Color.Transparent.toArgb(),
        ),
        navigationBarStyle = SystemBarStyle.auto(
            lightScrim = Color.Transparent.toArgb(),
            darkScrim = Color.Transparent.toArgb(),
        ),
    )
}

private fun Color.isDark() = (red * 299 + green * 587 + blue * 114) / 1000 < 0.5
