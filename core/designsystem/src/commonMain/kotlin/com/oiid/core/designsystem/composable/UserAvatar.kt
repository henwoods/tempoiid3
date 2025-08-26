package com.oiid.core.designsystem.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import com.oiid.core.designsystem.generated.resources.Res
import com.oiid.core.designsystem.generated.resources.default_avatar
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.base.ui.rememberImageLoader
import org.jetbrains.compose.resources.imageResource

enum class UserAvaterType {
    Profile,
    Primary,
    Secondary,
    Tertiary,
}

@Composable
fun UserAvatar(modifier: Modifier = Modifier, type: UserAvaterType, imageUrl: String? = null, name: String? = null) {
    val imageLoader = rememberImageLoader(LocalPlatformContext.current)
    val size = when (type) {
        UserAvaterType.Profile -> 128.dp
        UserAvaterType.Primary -> 42.dp
        UserAvaterType.Secondary -> 38.dp
        UserAvaterType.Tertiary -> 24.dp
    }

    val padding = when (type) {
        UserAvaterType.Profile -> 24.dp
        UserAvaterType.Primary -> 8.dp
        UserAvaterType.Secondary -> 8.dp
        UserAvaterType.Tertiary -> 2.dp
    }

    val colors = when (type) {
        UserAvaterType.Profile -> colorScheme.primary
        UserAvaterType.Primary -> colorScheme.primary
        UserAvaterType.Secondary -> colorScheme.secondary
        UserAvaterType.Tertiary -> colorScheme.tertiary
    }

    Box(
        modifier = modifier.size(size).clip(CircleShape)
            .background(colors.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center,
    ) {
        if (!imageUrl.isNullOrEmpty()) {
            SubcomposeAsyncImage(
                contentScale = ContentScale.Crop,
                model = imageUrl,
                contentDescription = "User Avatar",
                modifier = Modifier.size(size),
                imageLoader = imageLoader,
                loading = {
                    Image(
                        modifier = Modifier.size(size).background(colorScheme.primary).padding(padding),
                        contentScale = ContentScale.Inside,
                        colorFilter = ColorFilter.tint(colorScheme.onPrimary),
                        bitmap = imageResource(Res.drawable.default_avatar),
                        contentDescription = "Default Avatar",
                    )
                },
            )
        } else {
            Image(
                modifier = Modifier.size(size).background(colorScheme.gradients.gradient).padding(padding),
                contentScale = ContentScale.Inside,
                colorFilter = ColorFilter.tint(colorScheme.onPrimary),
                bitmap = imageResource(Res.drawable.default_avatar),
                contentDescription = "Default Avatar",
            )
        }
    }
}
