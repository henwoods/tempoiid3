package com.oiid.core.config

import com.oiid.core.common.CMPBuildConfig
import com.oiid.core.designsystem.theme.OiidColorScheme
import com.oiid.core.model.api.onboarding.AppOnboardingConfig
import com.oiid.core.model.api.onboarding.WelcomeScreen
import oiid.core.base.platform.BuildConfig

actual fun artistId(): String {
    return if (CMPBuildConfig.DEBUG_MODE) BuildConfig.ARTIST_ID_DEV else BuildConfig.ARTIST_ID_PROD
}

actual fun onboarding(): AppOnboardingConfig {
    return AppOnboardingConfig(
        artistId = artistId(),
        welcomeScreens = listOf(
            WelcomeScreen(
                body = "welcome_1_body",
                title = "welcome_1_title",
                image = "welcome_1",
            ),
            WelcomeScreen(
                body = "welcome_2_body",
                title = "welcome_2_title",
                image = "welcome_2",
            ),
            WelcomeScreen(
                body = "welcome_3_body",
                title = "welcome_3_title",
                image = "welcome_3",
            ),
            WelcomeScreen(
                body = "welcome_4_body",
                title = "welcome_4_title",
                image = "welcome_4",
            ),
        ),
        superFanSingular = "superfan_singular",
        superFanPlural = "superfan_plural",
    )
}

actual fun oiidTheme(): OiidColorScheme {
    return when (com.oiid.core.config.BuildConfig.ARTIST_NAME) {
        "enslaved" -> return OiidColorScheme.Nocturne
        "orionsbelte" -> return OiidColorScheme.Mezzo
        else -> return OiidColorScheme.Nocturne
    }
}
