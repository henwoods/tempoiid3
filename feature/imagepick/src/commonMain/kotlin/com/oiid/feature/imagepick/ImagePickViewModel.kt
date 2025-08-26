package com.oiid.feature.imagepick

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFilePicker
import kotlinx.coroutines.launch

class ImagePickViewModel() : ViewModel() {
    fun startPickingImage(onImagePicked: (file: PlatformFile?) -> Unit) {
        viewModelScope.launch {
            val file = FileKit.openFilePicker(type = FileKitType.Image)
            onImagePicked(file)
        }
    }
}
