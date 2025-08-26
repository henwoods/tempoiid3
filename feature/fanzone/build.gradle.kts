plugins {
    alias(libs.plugins.cmp.feature.convention)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.oiid.feature.fanzone"
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.coreBase.platform)
            implementation(projects.coreBase.ui)
            implementation(projects.core.ui)
            implementation(projects.core.model)
            implementation(projects.core.common)
            implementation(projects.core.config)
            implementation(projects.core.designsystem)
            implementation(projects.core.network)
            implementation(projects.core.datastore)
            implementation(projects.coreBase.datastore)
            implementation(projects.feature.feed)

            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(compose.materialIconsExtended)

            implementation(libs.kermit.logging)
            implementation(libs.coil.kt.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.ktorfit.lib)

            implementation(libs.composables.core)
        }
    }
}