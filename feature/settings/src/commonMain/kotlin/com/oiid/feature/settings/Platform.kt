package com.oiid.feature.settings

expect fun getPlatform(): Platform

enum class Platform {
    Android,
    Desktop,
    IOS,
    JS,
    Wasm,
}

expect fun supportsDynamicTheming(): Boolean
