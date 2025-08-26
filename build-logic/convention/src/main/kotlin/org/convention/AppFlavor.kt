package org.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor

@Suppress("EnumEntryName")
enum class FlavorDimension {
    artist
}

@Suppress("EnumEntryName")
enum class AppFlavor(
    val dimension: FlavorDimension,
    val applicationIdSuffix: String? = null,
    val artistIdDev: String? = null,
    val artistIdProd: String? = null,
    val artistName: String? = null
) {
    enslaved(
        FlavorDimension.artist,
        applicationIdSuffix = ".enslaved",
        artistIdDev = "artist_6e525763",
        artistIdProd = "artist_70291deb",
        artistName = "enslaved"
    ),
    orionsbelte(
        FlavorDimension.artist,
        applicationIdSuffix = ".orionsbelte",
        artistIdDev = "artist_d0287a90",
        artistIdProd = "artist_d0287a90",
        artistName = "orionsbelte"
    ),
    aurora(
        FlavorDimension.artist,
        applicationIdSuffix = ".aurora",
        artistIdDev = "artist_d0287a90",
        artistIdProd = "artist_d0287a90",
        artistName = "aurora"
    )
}

fun configureFlavors(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: AppFlavor) -> Unit = {},
) {
    commonExtension.apply {
        flavorDimensions += FlavorDimension.artist.name

        productFlavors {
            AppFlavor.values().forEach { flavor ->
                create(flavor.name) {
                    dimension = flavor.dimension.name

                    buildConfigField("String", "ARTIST_ID_DEV", "\"${flavor.artistIdDev}\"")
                    buildConfigField("String", "ARTIST_ID_PROD", "\"${flavor.artistIdProd}\"")
                    buildConfigField("String", "ARTIST_NAME", "\"${flavor.artistName}\"")

                    flavorConfigurationBlock(this, flavor)
                    if (this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                        if (flavor.applicationIdSuffix != null) {
                            applicationIdSuffix = flavor.applicationIdSuffix
                            versionNameSuffix = "-${flavor.name}"
                        }
                    }
                }
            }
        }
    }
}
