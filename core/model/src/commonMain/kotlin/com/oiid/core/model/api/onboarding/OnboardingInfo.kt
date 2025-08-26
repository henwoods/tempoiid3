package com.oiid.core.model.api.onboarding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

enum class OnboardingDataSource {
    Local,
    Remote,
}

@Serializable
data class WelcomeScreen(
    @SerialName("body") val body: String,
    @SerialName("title") val title: String,
    @SerialName("image") val image: String,
)

@Serializable
data class AppOnboardingConfig(
    @SerialName("artistId") val artistId: String,
    @SerialName("welcomeScreens") val welcomeScreens: List<WelcomeScreen>,
    @SerialName("superFanSingular") val superFanSingular: String,
    @SerialName("superFanPlural") val superFanPlural: String,
    @Transient val source: OnboardingDataSource = OnboardingDataSource.Local,
)
