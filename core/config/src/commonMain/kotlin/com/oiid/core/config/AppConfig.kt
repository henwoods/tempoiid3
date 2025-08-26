package com.oiid.core.config

import com.oiid.core.designsystem.theme.OiidColorScheme
import com.oiid.core.model.api.onboarding.AppOnboardingConfig

expect fun artistId(): String

expect fun onboarding(): AppOnboardingConfig

expect fun oiidTheme(): OiidColorScheme
