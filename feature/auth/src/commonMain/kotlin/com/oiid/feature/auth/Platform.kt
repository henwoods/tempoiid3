package com.oiid.feature.auth

expect fun getPlatform(): Platform

enum class Platform {
    Android,
    Desktop,
    IOS,
    JS,
    Wasm,
}

expect fun supportsDynamicTheming(): Boolean
