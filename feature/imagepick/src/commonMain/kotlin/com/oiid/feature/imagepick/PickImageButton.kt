package com.oiid.feature.imagepick

import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.vinceglb.filekit.PlatformFile
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PickImageButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onImagePicked: (file: PlatformFile?) -> Unit,
    viewModel: ImagePickViewModel = koinViewModel(),
) {
    FilledTonalIconButton(
        modifier = modifier,
        onClick = {
            viewModel.startPickingImage {
                onImagePicked(it)
            }
        },
    ) {
        Icon(imageVector = imageVector, contentDescription = "Pick Image")
    }
}
