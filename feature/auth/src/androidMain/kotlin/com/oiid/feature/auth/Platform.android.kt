package com.oiid.feature.auth

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

actual fun getPlatform(): Platform {
    return Platform.Android
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
actual fun supportsDynamicTheming(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}
