package com.oiid.feature.feed.comment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import com.oiid.core.designsystem.composable.UserAvatar
import com.oiid.core.designsystem.composable.UserAvaterType
import oiid.core.base.designsystem.theme.OiidTheme

@Composable
fun CommentInputField(
    value: String,
    label: String,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit,
    userName: String? = null,
    userAvatarImageUrl: String,
    onSendClick: () -> Unit,
    onCancel: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(
            start = OiidTheme.spacing.xl,
            end = OiidTheme.spacing.md,
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(OiidTheme.spacing.sm),
    ) {
        UserAvatar(type = UserAvaterType.Secondary, imageUrl = userAvatarImageUrl, name = userName)
        ReplyInput(
            value = value,
            focusRequester = focusRequester,
            onValueChange = onValueChange,
            label = label,
            onSendClick = onSendClick,
            onCancel = onCancel,
        )
    }
}
