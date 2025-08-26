plugins {
    alias(libs.plugins.kmp.library.convention)
    alias(libs.plugins.kotlin.parcelize)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.coreBase.platform)
            implementation(projects.core.common)
            implementation(projects.core.datastore)
            implementation(projects.core.model)
            implementation(projects.core.network)
            api(projects.core.designsystem)
            api(projects.coreBase.designsystem)
        }

        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.tracing.ktx)
            implementation(libs.koin.android)
        }
    }
}