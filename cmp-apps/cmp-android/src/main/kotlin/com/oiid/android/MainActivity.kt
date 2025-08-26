package com.oiid.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import cmp.shared.SharedApp
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init
import oiid.core.base.platform.context.LocalContext
import oiid.core.base.platform.update.AppUpdateManager
import oiid.core.base.platform.update.AppUpdateManagerImpl
import oiid.core.base.ui.ShareUtils

class MainActivity : ComponentActivity() {

    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appUpdateManager = AppUpdateManagerImpl(this)

        installSplashScreen()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        ShareUtils.setActivityProvider { return@setActivityProvider this }

        FileKit.init(this)

        setContent {
            SystemBarsEffect()

            SharedApp()
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.checkForResumeUpdateState()
    }
}

@Composable
fun SystemBarsEffect() {
    val context = LocalContext.current
    val isLight = isSystemInDarkTheme()
    LaunchedEffect(isLight) {
        val activity = context as? ComponentActivity ?: return@LaunchedEffect

        activity.enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                Color.Transparent.toArgb(),
            ),
            navigationBarStyle = SystemBarStyle.dark(
                Color.Transparent.toArgb(),
            ),
        )
    }
}
