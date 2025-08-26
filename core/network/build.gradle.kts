import com.google.devtools.ksp.gradle.KspAATask

plugins {
    alias(libs.plugins.kmp.library.convention)
    alias(libs.plugins.ktrofit)
    id("kotlinx-serialization")
    id("com.google.devtools.ksp")
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
}

// Need this temporarily while latest KSP bugs are resolved
project.tasks.withType(KspAATask::class.java).configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        if (name == "kspDebugKotlinAndroid") {
            enabled = false
        }
        if (name == "kspReleaseKotlinAndroid") {
            enabled = false
        }
        if (name == "kspKotlinIosSimulatorArm64") {
            enabled = false
        }
        if (name == "kspKotlinIosX64") {
            enabled = false
        }
        if (name == "kspKotlinIosArm64") {
            enabled = false
        }
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.model)
            implementation(projects.core.datastore)
            implementation(projects.coreBase.network)

            implementation(libs.kotlinx.serialization.json)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.ktorfit.lib)
            implementation(libs.koin.core)
            implementation(libs.squareup.okio)
            implementation(libs.kermit.logging)

            implementation(libs.cognito.idp)

            implementation(libs.multiplatform.settings)

        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.android)
            implementation(libs.koin.android)
        }

        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.ktorfit.ksp)
    add("kspAndroid", libs.ktorfit.ksp)
    add("kspJs", libs.ktorfit.ksp)
    add("kspWasmJs", libs.ktorfit.ksp)
    add("kspDesktop", libs.ktorfit.ksp)
    add("kspIosX64", libs.ktorfit.ksp)
    add("kspIosArm64", libs.ktorfit.ksp)
    add("kspIosSimulatorArm64", libs.ktorfit.ksp)
}