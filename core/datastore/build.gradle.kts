plugins {
    alias(libs.plugins.kmp.library.convention)
    id("kotlinx-serialization")
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.core)
            implementation(projects.core.model)
            implementation(projects.core.common)

            api(projects.coreBase.datastore)

            implementation(libs.multiplatform.settings)
        }


        // multiplatform settings doesn't currently support JS
        nonJsCommonMain.dependencies {
            implementation(libs.multiplatform.settings.datastore)
        }
    }
}
