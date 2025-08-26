plugins {
    alias(libs.plugins.kmp.library.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.oiid.base.network"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.ktor.client.core)
            api(libs.ktor.client.logging)
            api(libs.ktor.client.content.negotiation)
            api(libs.ktor.serialization.kotlinx.json)
            api(libs.ktor.client.auth)
            api(libs.ktorfit.lib)
        }

        androidMain.dependencies {
            api(libs.ktor.client.okhttp)
            api(libs.koin.android)
        }

        nativeMain.dependencies {
            api(libs.ktor.client.darwin)
        }

        desktopMain.dependencies {
            api(libs.ktor.client.okhttp)
        }

        jsMain.dependencies {
            api(libs.ktor.client.js)
        }

        wasmJsMain.dependencies {
            api(libs.ktor.client.js)
        }
    }
}