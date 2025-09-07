package com.oiid.feature.feed.composables

import androidx.compose.runtime.Composable
import com.oiid.core.config.oiidTheme
import com.oiid.core.designsystem.composable.UserAvatar
import com.oiid.core.designsystem.composable.UserAvaterType
import com.oiid.core.designsystem.theme.OiidTheme

@Composable
fun FeedUserAvatar(
    type: UserAvaterType,
    imageUrl: String,
) {
    OiidTheme(oiidColorScheme = oiidTheme(), false) {
        UserAvatar(type = type, imageUrl = imageUrl)
    }
}