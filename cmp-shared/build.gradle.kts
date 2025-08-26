plugins {
    alias(libs.plugins.kmp.library.convention)
    alias(libs.plugins.cmp.feature.convention)
    alias(libs.plugins.kotlinCocoapods)
}

kotlin {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            optimized = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.cmpNavigation)
            implementation(compose.components.resources)
            implementation(projects.coreBase.platform)
            implementation(projects.coreBase.ui)
            implementation(projects.core.config)

            implementation(libs.coil.kt.compose)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(compose.desktop.common)
        }
    }

    cocoapods {
        summary = "Oiid"
        homepage = "http://www.oiid.com"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../cmp-apps/cmp-ios/Podfile")

        framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
}

android {
    namespace = "cmp.shared"
}

compose.resources {
    publicResClass = true
    generateResClass = always
    packageOfResClass = "cmp.shared.generated.resources"
}