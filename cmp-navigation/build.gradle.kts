plugins {
    alias(libs.plugins.kmp.library.convention)
    alias(libs.plugins.cmp.feature.convention)
    alias(libs.plugins.kmp.koin.convention)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.coreBase.ui)
            implementation(projects.core.network)
            implementation(projects.core.data)
            implementation(projects.core.model)
            implementation(projects.core.config)
            implementation(projects.core.designsystem)
            implementation(projects.core.common)
            implementation(projects.core.datastore)
            implementation(projects.coreBase.platform)

            implementation(projects.feature.feed)
            implementation(projects.feature.settings)
            implementation(projects.feature.auth)
            implementation(projects.feature.onboarding)
            implementation(projects.feature.settings)
            implementation(projects.feature.imagepick)

            implementation(compose.material3)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)
            implementation(libs.window.size)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.kotlinx.serialization.json)
            implementation(libs.coil.kt.compose)

            implementation(libs.haze)
            implementation(libs.haze.materials)
        }
    }
}

android {
    namespace = "cmp.navigation"
}

compose.resources {
    publicResClass = true
    generateResClass = always
    packageOfResClass = "cmp.navigation.generated.resources"
}