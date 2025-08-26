plugins {
    alias(libs.plugins.kmp.library.convention)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "oiid.core.base.designsystem"
}

kotlin {
    sourceSets{
        androidMain.dependencies {
            implementation(libs.androidx.compose.ui.tooling)
        }
        commonMain.dependencies {
            implementation(libs.kermit.logging)

            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.foundation)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)

            api(compose.material3AdaptiveNavigationSuite)
            api(libs.jetbrains.compose.material3.adaptive)
            api(libs.jetbrains.compose.material3.adaptive.layout)
            api(libs.jetbrains.compose.material3.adaptive.navigation)

            implementation(libs.jb.lifecycleViewmodel)
            implementation(libs.window.size)
            implementation(libs.ui.backhandler)

            implementation(libs.coil.kt.compose)
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = always
    packageOfResClass = "com.oiid.core.base.designsystem.generated.resources"
}