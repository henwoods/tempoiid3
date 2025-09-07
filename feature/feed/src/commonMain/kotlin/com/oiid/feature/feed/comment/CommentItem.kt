package com.oiid.feature.feed.comment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.oiid.core.designsystem.composable.UserAvaterType
import com.oiid.core.model.PostComment
import com.oiid.core.model.PostItem
import com.oiid.feature.feed.composables.FeedUserAvatar
import oiid.core.base.designsystem.theme.OiidTheme.spacing
import oiid.core.ui.PostIntent

@Composable
fun CommentItem(
    modifier: Modifier = Modifier,
    comment: PostComment,
    post: PostItem,
    onStartReplying: (PostComment) -> Unit,
    onHandleIntent: (PostIntent) -> Unit,
) {
    CommentCard(modifier = modifier, !comment.parentCommentId.isNullOrEmpty()) {
        Row(
            modifier = Modifier.padding(spacing.md),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(spacing.md),
        ) {
            FeedUserAvatar(UserAvaterType.Primary, comment.profileImage)

            Column(verticalArrangement = Arrangement.spacedBy(spacing.xs)) {
                CommentContent(
                    comment = comment,
                    post = post,
                    onStartReplying = onStartReplying,
                    onHandleIntent = onHandleIntent,
                )
            }
        }
    }
}
