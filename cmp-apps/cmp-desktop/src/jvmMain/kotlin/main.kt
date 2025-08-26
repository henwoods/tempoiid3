import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cmp.shared.SharedApp
import cmp.shared.utils.initKoin
import io.github.vinceglb.filekit.FileKit
import oiid.core.base.platform.context.AppContextModule

fun main() {
    application {
        initKoin(listOf(AppContextModule))

        FileKit.init("Oiid")

        val windowState = rememberWindowState(size = DpSize(400.dp, 820.dp))

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Aurora",
        ) {
            SharedApp()
        }
    }
}
