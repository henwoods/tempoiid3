import org.convention.AppBuildType
import org.convention.dynamicVersion

plugins {
    alias(libs.plugins.android.application.convention)
    alias(libs.plugins.android.application.compose.convention)
    alias(libs.plugins.android.application.flavors.convention)
    alias(libs.plugins.buildconfig)
    id("com.google.devtools.ksp")
}

val packageNameSpace: String = libs.versions.androidPackageNamespace.get()

android {
    namespace = "com.oiid.android"

    defaultConfig {
        applicationId = packageNameSpace
        versionName = System.getenv("VERSION") ?: project.dynamicVersion
        versionCode = (project.findProperty("VERSION_CODE") as? String)?.toIntOrNull() ?: 1
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file(System.getenv("KEYSTORE_PATH") ?: "../keystores/release_keystore.keystore")
            storePassword = System.getenv("KEYSTORE_PASSWORD") ?: ""
            keyAlias = System.getenv("KEYSTORE_ALIAS") ?: "oiid"
            keyPassword = System.getenv("KEYSTORE_ALIAS_PASSWORD") ?: ""
            enableV1Signing = true
            enableV2Signing = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = AppBuildType.DEBUG.applicationIdSuffix
        }

        release {
            isMinifyEnabled = true
            applicationIdSuffix = AppBuildType.RELEASE.applicationIdSuffix
            isShrinkResources = true
            isDebuggable = false
            isJniDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        dataBinding = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.cmpShared)
    implementation(projects.core.ui)
    implementation(projects.coreBase.platform)
    implementation(projects.coreBase.ui)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.tracing.ktx)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)

    implementation(libs.app.update.ktx)
    implementation(libs.app.update)

    implementation(libs.filekit.core)
    implementation(libs.filekit.dialogs.compose)
    implementation(libs.coil.kt)

    runtimeOnly(libs.androidx.compose.runtime)
    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.compose.ui.test)

    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.androidx.test.ext.junit)

    testImplementation(libs.koin.test.junit4)
}