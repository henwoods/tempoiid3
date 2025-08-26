plugins {
    alias(libs.plugins.cmp.feature.convention)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.oiid.settings"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.datastore)
            implementation(projects.feature.imagepick)
            implementation(projects.core.model)
            implementation(projects.core.network)
            implementation(projects.core.datastore)
            implementation(projects.coreBase.ui)
            implementation(projects.feature.onboarding)

            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.coil.network.ktor)
            implementation(libs.coil.kt.compose)
            implementation(libs.kermit.logging)
            implementation(libs.ktorfit.lib)

            implementation(libs.filekit.core)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
