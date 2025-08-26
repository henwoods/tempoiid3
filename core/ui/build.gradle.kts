plugins {
    alias(libs.plugins.kmp.library.convention)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            api(libs.androidx.metrics)
            implementation(libs.androidx.browser)
            implementation(libs.androidx.compose.runtime)
        }

        commonMain.dependencies {
            implementation(projects.core.designsystem)
            implementation(projects.core.model)
            implementation(projects.coreBase.platform)
            implementation(projects.core.common)
            implementation(projects.coreBase.platform)
            implementation(libs.jb.composeViewmodel)
            implementation(libs.jb.lifecycleViewmodel)
            implementation(libs.jb.lifecycleViewmodelSavedState)
            implementation(libs.coil.kt)
            implementation(libs.coil.kt.compose)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.jb.composeNavigation)
            implementation(libs.filekit.dialogs.compose)
            implementation(libs.filekit.core)
        }
        androidInstrumentedTest.dependencies {
            implementation(libs.bundles.androidx.compose.ui.test)
        }
    }
}
dependencies {
    debugImplementation(compose.uiTooling)
}

compose.resources {
    publicResClass = true
    generateResClass = always
    packageOfResClass = "com.oiid.core.ui.generated.resources"
}