package com.oiid.feature.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import com.oiid.core.designsystem.composable.LinearProgress
import com.oiid.core.designsystem.composable.OiidButton
import com.oiid.core.designsystem.composable.UnderlineTextField
import com.oiid.core.designsystem.composable.UserAvatar
import com.oiid.core.designsystem.composable.UserAvaterType
import com.oiid.core.designsystem.generated.resources.Res
import com.oiid.core.designsystem.generated.resources.header
import com.oiid.feature.imagepick.PickImageButton
import io.github.vinceglb.filekit.PlatformFile
import oiid.core.base.designsystem.theme.OiidTheme
import oiid.core.base.ui.rememberImageLoader
import org.jetbrains.compose.resources.imageResource

@Composable
internal fun EditingProfileScreen(
    modifier: Modifier = Modifier,
    onImagePicked: (type: String, file: PlatformFile) -> Unit,
    onUpdateEditableName: (String) -> Unit,
    onUpdateEditableBio: (String) -> Unit,
    onSaveProfileChanges: () -> Unit,
    onCancelEditing: () -> Unit,
    editProfileState: EditProfileUiState,
) {
    val imageLoader = rememberImageLoader(LocalPlatformContext.current)
    val profileData = editProfileState.editedProfileData

    Surface {
        BoxWithConstraints(
            modifier = modifier.fillMaxSize().imePadding(),
        ) {
            val avatarSize = 128.dp
            val avatarSizePx = with(LocalDensity.current) { avatarSize.toPx() }

            val imageHeightPercentage = 0.55f
            val splitY = constraints.maxHeight * imageHeightPercentage
            val avatarTopOffset = splitY - (avatarSizePx / 2)
            val avatarEditOffsetY = (splitY - (avatarSizePx)) + 72
            val avatarEditOffsetX = (avatarSizePx / 2) - 48

            val avatarOffsetDp = with(LocalDensity.current) { avatarTopOffset.toDp() }
            val avatarEditOffsetYDp = with(LocalDensity.current) { avatarEditOffsetY.toDp() }
            val avatarEditOffsetXDp = with(LocalDensity.current) { avatarEditOffsetX.toDp() }
            val splitDp = with(LocalDensity.current) { splitY.toDp() }

            if (editProfileState.isLoading) {
                LinearProgress(
                    modifier = Modifier.align(Alignment.TopCenter).offset(y = splitDp),
                )
            }

            if (profileData?.headerImageUrl.isNullOrEmpty()) {
                Image(
                    bitmap = imageResource(Res.drawable.header),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Header",
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(imageHeightPercentage).align(Alignment.TopCenter),
                )
            } else {
                SubcomposeAsyncImage(
                    model = profileData.headerImageUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = "Header",
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(imageHeightPercentage).align(Alignment.TopCenter),
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
                modifier = Modifier.fillMaxWidth().fillMaxHeight(imageHeightPercentage).align(Alignment.TopCenter),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PickImageButton(
                    imageVector = Icons.Outlined.AddPhotoAlternate,
                    onImagePicked = {
                        if (it != null) {
                            onImagePicked("header", it)
                        }
                    },
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(1f - imageHeightPercentage)
                    .align(Alignment.BottomCenter)
                    .padding(top = avatarSize / 2),
            ) {
                UnderlineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = editProfileState.editedProfileData?.name ?: "",
                    onValueChange = { onUpdateEditableName(it) },
                    textStyle = OiidTheme.typography.bodyLarge.copy(
                        color = OiidTheme.colorScheme.onSurface,
                    ),
                    label = "Name",
                )

                UnderlineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = editProfileState.editedProfileData?.bio ?: "",
                    onValueChange = { onUpdateEditableBio(it) },
                    textStyle = OiidTheme.typography.bodyLarge.copy(
                        color = OiidTheme.colorScheme.onSurface,
                    ),
                    label = "Bio",
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(OiidTheme.spacing.md).align(Alignment.TopEnd),
                horizontalArrangement = Arrangement.spacedBy(OiidTheme.spacing.md, Alignment.End),
            ) {
                OiidButton(
                    text = "Cancel",
                    enabled = !editProfileState.isLoading,
                    onClick = { onCancelEditing() },
                )
                OiidButton(
                    text = "Done",
                    enabled = !editProfileState.isLoading,
                    onClick = { onSaveProfileChanges() },
                )
            }

            UserAvatar(
                type = UserAvaterType.Profile,
                imageUrl = profileData?.profileImageUrl,
                modifier = Modifier.size(avatarSize).align(Alignment.TopCenter).offset(y = avatarOffsetDp),
            )

            PickImageButton(
                modifier = Modifier.align(Alignment.TopCenter)
                    .offset(y = avatarEditOffsetYDp, x = avatarEditOffsetXDp),
                imageVector = Icons.Outlined.AddAPhoto,
                onImagePicked = {
                    if (it != null) {
                        onImagePicked("profile", it)
                    }
                },
            )
        }
    }
}
