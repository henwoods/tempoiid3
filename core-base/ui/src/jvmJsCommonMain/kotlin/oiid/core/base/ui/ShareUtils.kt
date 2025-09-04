package oiid.core.base.ui

import androidx.compose.ui.graphics.ImageBitmap

actual object ShareUtils {
    actual fun shareText(text: String) {
    }

    actual suspend fun shareImage(title: String, image: ImageBitmap) {
    }

    actual suspend fun shareImage(title: String, byte: ByteArray) {
    }
}
