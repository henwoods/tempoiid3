package com.oiid.feature.feed.comment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.oiid.core.designsystem.composable.UserAvatar
import com.oiid.core.designsystem.composable.UserAvaterType
import com.oiid.core.model.PostComment
import oiid.core.base.designsystem.theme.OiidTheme
import oiid.core.ui.PostIntent

@Composable
fun CommentItem(
    modifier: Modifier = Modifier,
    comment: PostComment,
    onStartReplying: (PostComment) -> Unit,
    onHandleIntent: (PostIntent) -> Unit,
) {
    CommentCard(modifier = modifier, !comment.parentCommentId.isNullOrEmpty()) {
        Row(
            modifier = Modifier.padding(OiidTheme.spacing.md),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(OiidTheme.spacing.md),
        ) {
            UserAvatar(type = UserAvaterType.Primary, imageUrl = comment.profileImage, name = comment.name)
            Column(verticalArrangement = Arrangement.spacedBy(OiidTheme.spacing.xs)) {
                CommentContent(
                    comment = comment,
                    onStartReplying = onStartReplying,
                    onHandleIntent = onHandleIntent,
                )
            }
        }
    }
}
