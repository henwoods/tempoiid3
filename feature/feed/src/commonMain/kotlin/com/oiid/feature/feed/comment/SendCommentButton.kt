package com.oiid.feature.feed.comment

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable

@Composable
fun SendCommentButton(enabled: Boolean, onSendClick: () -> Unit) {
    IconButton(
        colors = IconButtonDefaults.iconButtonColors(),
        onClick = onSendClick,
        enabled = enabled,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "Send comment",
        )
    }
}
