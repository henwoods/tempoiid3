package cmp.shared

import androidx.compose.ui.window.ComposeUIViewController
import cmp.shared.utils.initKoin

fun viewController() = ComposeUIViewController(
    configure = {
        initKoin()
    },
) {
    SharedApp()
}
