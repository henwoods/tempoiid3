// Reading local properties only required for accessing cognito idp packages from fork
// https://github.com/EpicSquid/cognito-idp. When the the PR is merged and published
// https://github.com/Liftric/cognito-idp/pull/73 these and the EpicSquidCognitoIDP
// maven dependency host can be removed.
import java.util.Properties

val localProperties = Properties()
val localPropertiesFile = file("local.properties")

if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.PREFER_PROJECT
    repositories {
        mavenLocal()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven("https://jogamp.org/deployment/maven")
        mavenCentral()
        gradlePluginPortal()
        maven {
            name = "EpicSquidCognitoIDP"
            url = uri("https://maven.pkg.github.com/EpicSquid/cognito-idp")

            credentials {
                username = localProperties.getProperty("gpr.user")
                password = localProperties.getProperty("gpr.token")
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("1.0.0")
    id("org.ajoberstar.reckon.settings") version("0.19.2")
}

buildCache {
    local {
        isEnabled = true
        directory = File(rootDir, "build-cache")
    }
}

extensions.configure<org.ajoberstar.reckon.gradle.ReckonExtension> {
    setDefaultInferredScope("patch")
    stages("beta", "rc", "final")
    setScopeCalc { java.util.Optional.of(org.ajoberstar.reckon.core.Scope.PATCH) }
    setScopeCalc(calcScopeFromProp().or(calcScopeFromCommitMessages()))
    setStageCalc(calcStageFromProp())
    setTagWriter { it.toString() }
}

rootProject.name = "cmp-oiid"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":cmp-shared")
include(":cmp-apps:cmp-android")
include(":cmp-apps:cmp-desktop")
include(":cmp-apps:cmp-web")
include(":cmp-navigation")

include(":core:config")
include(":core:data")
include(":core:domain")
include(":core:datastore")
include(":core:designsystem")
include(":core:ui")
include(":core:common")
include(":core:network")
include(":core:model")

include(":feature:feed")
include(":feature:auth")
include(":feature:events")
include(":feature:onboarding")
include(":feature:settings")
include(":feature:imagepick")
include(":feature:player")
include(":feature:fanzone")

include(":core-base:datastore")
include(":core-base:database")
include(":core-base:network")
include(":core-base:designsystem")
include(":core-base:platform")
include(":core-base:ui")

check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_21)) {
    """
    This project requires JDK 21+ but it is currently using JDK ${JavaVersion.current()}.
    Java Home: [${System.getProperty("java.home")}]
    https://developer.android.com/build/jdks#jdk-config-in-studio
    """.trimIndent()
}