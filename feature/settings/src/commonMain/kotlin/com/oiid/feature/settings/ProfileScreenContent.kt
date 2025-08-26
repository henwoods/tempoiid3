package com.oiid.feature.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Slideshow
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import com.oiid.core.designsystem.composable.DropdownOption
import com.oiid.core.designsystem.composable.LinearProgress
import com.oiid.core.designsystem.composable.PopupMenu
import com.oiid.core.designsystem.composable.UserAvatar
import com.oiid.core.designsystem.composable.UserAvaterType
import com.oiid.core.designsystem.generated.resources.Res
import com.oiid.core.designsystem.generated.resources.header
import com.oiid.core.designsystem.utils.onClick
import oiid.core.base.designsystem.theme.OiidTheme
import oiid.core.base.designsystem.theme.OiidTheme.spacing
import oiid.core.base.designsystem.theme.OiidTheme.typography
import oiid.core.base.ui.rememberImageLoader
import org.jetbrains.compose.resources.imageResource

@Composable
internal fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    profileUiState: ProfileUiState,
    onLogoutClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onOnboardingClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
) {
    val imageLoader = rememberImageLoader(LocalPlatformContext.current)

    BoxWithConstraints(modifier = modifier.fillMaxSize().background(OiidTheme.colorScheme.background)) {
        val avatarSize = 128.dp
        val avatarSizePx = with(LocalDensity.current) { avatarSize.toPx() }

        val imageHeightPercentage = 0.55f
        val splitY = constraints.maxHeight * imageHeightPercentage
        val avatarTopOffset = splitY - (avatarSizePx / 2)

        val avatarOffsetDp = with(LocalDensity.current) { avatarTopOffset.toDp() }
        val splitDp = with(LocalDensity.current) { splitY.toDp() }

        when (profileUiState) {
            is ProfileUiState.Loading -> {
                Image(
                    bitmap = imageResource(Res.drawable.header),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Header",
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(imageHeightPercentage).align(Alignment.TopCenter),
                )

                LinearProgress(
                    modifier = Modifier.align(Alignment.TopCenter).offset(y = splitDp),
                )
            }

            is ProfileUiState.Error -> {
                Text(
                    text = "Error: ${profileUiState.message}",
                    color = OiidTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center).padding(OiidTheme.spacing.md),
                )
            }

            is ProfileUiState.Success -> {
                val profileData = profileUiState.profileData

                if (profileData.headerImageUrl.isNullOrEmpty()) {
                    Image(
                        bitmap = imageResource(Res.drawable.header),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Header",
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(imageHeightPercentage)
                            .align(Alignment.TopCenter),
                    )
                } else {
                    SubcomposeAsyncImage(
                        model = profileData.headerImageUrl,
                        contentScale = ContentScale.Crop,
                        contentDescription = "Header",
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(imageHeightPercentage)
                            .align(Alignment.TopCenter),
                        imageLoader = imageLoader,
                        loading = {
                            Image(
                                bitmap = imageResource(Res.drawable.header),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Header",
                                modifier = Modifier.fillMaxWidth().fillMaxHeight(1f),
                            )
                        },
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(1f - imageHeightPercentage)
                        .align(Alignment.BottomCenter).padding(top = avatarSize / 2),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacing.md, Alignment.CenterHorizontally),
                        modifier = Modifier.fillMaxWidth().padding(OiidTheme.spacing.md).onClick(
                            onClick = {
                                onEditProfileClick()
                            },
                        ),
                    ) {
                        if (profileData.name.isEmpty()) {
                            Spacer(Modifier.size(spacing.lg))
                        }

                        Text(text = profileData.name.ifEmpty { "No name set" }, style = typography.headlineSmall)

                        if (profileData.name.isEmpty()) {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                modifier = Modifier.onClick(
                                    onClick = {
                                        onEditProfileClick()
                                    },
                                ),
                                contentDescription = "Edit profile",
                            )
                        }
                    }

                    BasicTextField(
                        modifier = Modifier.fillMaxSize().padding(horizontal = spacing.md),
                        value = profileData.bio.ifEmpty { "No bio set" },
                        onValueChange = { },
                        enabled = false,
                        textStyle = OiidTheme.typography.bodyMedium.copy(color = OiidTheme.colorScheme.onSurface),
                    )
                }

                UserAvatar(
                    type = UserAvaterType.Profile,
                    imageUrl = profileData.profileImageUrl,
                    modifier = Modifier.size(avatarSize).align(Alignment.TopCenter).offset(y = avatarOffsetDp),
                )
            }
        }

        PopupMenu(
            alignment = Alignment.TopEnd,
            finalSeparatorOffset = 2,
            modifier = Modifier.fillMaxWidth().padding(OiidTheme.spacing.md),
            options = listOf(
                DropdownOption("edit", "Edit profile", Icons.Outlined.Edit, true),
                DropdownOption("terms", "Terms and Conditions", Icons.Outlined.Info, true),
                DropdownOption("privacy", "Privacy Policy", Icons.Outlined.Policy, true),
                DropdownOption("onboarding", "Show Onboarding", Icons.Outlined.Slideshow, true),
                DropdownOption("logout", "Logout", Icons.AutoMirrored.Outlined.Logout, true),
                DropdownOption(
                    key = "delete_account",
                    text = "Delete account",
                    icon = Icons.Outlined.Warning,
                    enabled = true,
                    dangerous = true,
                ),
            ),
            onOptionClick = {
                when (it) {
                    "edit" -> onEditProfileClick()
                    "terms" -> onTermsClick()
                    "privacy" -> onPrivacyClick()
                    "onboarding" -> onOnboardingClick()
                    "logout" -> onLogoutClick()
                    "delete_account" -> onDeleteAccountClick()
                }
            },
        )
    }
}
