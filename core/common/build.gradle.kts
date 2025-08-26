import java.util.Properties


plugins {
    alias(libs.plugins.kmp.library.convention)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.buildconfig)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            api(libs.kermit.logging)
            api(libs.squareup.okio)
            api(libs.jb.kotlin.stdlib)
            api(libs.kotlinx.datetime)
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
        }
        commonTest.dependencies {
            implementation(libs.kotlinx.coroutines.test)
        }
        iosMain.dependencies {
            api(libs.kermit.simple)
        }
        desktopMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.kotlin.reflect)
        }
        jsMain.dependencies {
            api(libs.jb.kotlin.stdlib.js)
            api(libs.jb.kotlin.dom)
        }
    }
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

buildConfig {
    className("CMPBuildConfig")
    packageName = "com.oiid.core.common"

    useKotlinOutput {
        internalVisibility = false
    }


    buildConfigField("String", "X_API_KEY_DEV", "\"${localProperties.getProperty("x.api.key.dev", "")}\"")
    buildConfigField("String", "BASE_URL_DEV", "\"${localProperties.getProperty("baseurl.dev", "")}\"")
    buildConfigField("String", "HOST_URL_DEV", "\"${localProperties.getProperty("hosturl.dev", "")}\"")
    buildConfigField("boolean", "DEBUG_MODE", localProperties.getProperty("debug.mode", "false"))
    buildConfigField("String", "REGION_DEV", "\"${localProperties.getProperty("region.dev", "")}\"")
    buildConfigField("String", "CLIENT_ID_DEV", "\"${localProperties.getProperty("clientid.dev", "")}\"")

    buildConfigField("String", "X_API_KEY_PROD", "\"${localProperties.getProperty("x.api.key.prod", "")}\"")
    buildConfigField("String", "BASE_URL_PROD", "\"${localProperties.getProperty("baseurl.prod", "")}\"")
    buildConfigField("String", "HOST_URL_PROD", "\"${localProperties.getProperty("hosturl.prod", "")}\"")
    buildConfigField("String", "REGION_PROD", "\"${localProperties.getProperty("region.prod", "")}\"")
    buildConfigField("String", "CLIENT_ID_PROD", "\"${localProperties.getProperty("clientid.prod", "")}\"")
}
