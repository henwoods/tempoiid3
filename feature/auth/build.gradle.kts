plugins {
    alias(libs.plugins.cmp.feature.convention)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.oiid.auth"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.datastore)
            implementation(projects.core.model)
            implementation(projects.coreBase.ui)
            implementation(projects.core.network)
            implementation(projects.core.common)
            implementation(projects.coreBase.network)

            implementation(compose.ui)
            implementation(compose.materialIconsExtended)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.kotlinx.serialization.json)

            implementation(libs.coil.kt.compose)
            implementation(libs.kermit.logging)
        }
    }
}
