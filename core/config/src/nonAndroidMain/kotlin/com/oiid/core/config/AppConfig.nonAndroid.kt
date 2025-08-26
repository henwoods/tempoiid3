package com.oiid.core.config

import com.oiid.core.designsystem.theme.OiidColorScheme
import com.oiid.core.model.api.onboarding.AppOnboardingConfig

actual fun artistId(): String {
    return "artist_6e525763"
}

actual fun onboarding(): AppOnboardingConfig {
    return AppOnboardingConfig(
        artistId = artistId(),
        welcomeScreens = emptyList(),
        superFanSingular = "Superfan",
        superFanPlural = "Superfans",
    )
}

actual fun oiidTheme(): OiidColorScheme {
    return OiidColorScheme.Nocturne
}