plugins {
    alias(libs.plugins.kmp.library.convention)
}

android {
    namespace = "oiid.core.base.database"
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.room.runtime)
        }

        desktopMain.dependencies {
            implementation(libs.androidx.room.runtime)
        }

        nativeMain.dependencies {
            implementation(libs.androidx.room.runtime)
        }

        nonJsCommonMain.dependencies {
            implementation(libs.androidx.room.runtime)
        }
    }
}
