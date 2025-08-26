package com.oiid.feature.feed.comment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import com.oiid.core.designsystem.composable.UnderlineTextField

@Composable
fun ReplyInput(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onCancel: () -> Unit,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            UnderlineTextField(
                modifier = Modifier.weight(1f),
                focusRequester = focusRequester,
                label = label,
                value = value,
                onValueChange = onValueChange,
                onCancel = onCancel,
            )
            AnimatedVisibility(value.isNotEmpty()) {
                SendCommentButton(value.isNotBlank(), onSendClick)
            }
        }
    }
}
