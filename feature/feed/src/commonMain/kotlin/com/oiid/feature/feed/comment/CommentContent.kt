package com.oiid.feature.feed.comment

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.composeunstyled.Text
import com.oiid.core.common.formatRelativeTime
import com.oiid.core.designsystem.composable.FeedItemDetails
import com.oiid.core.model.PostComment
import com.oiid.feature.feed.CommentActionsPopup
import com.oiid.feature.feed.list.PostIntent

@Composable
fun CommentContent(
    comment: PostComment,
    onStartReplying: (comment: PostComment) -> Unit,
    onHandleIntent: (PostIntent) -> Unit,
) {
    FeedItemDetails(
        name = comment.name,
        createdAt = formatRelativeTime(comment.createdAt),
        actions = {
            CommentActionsPopup(comment.commentId,  { onStartReplying(comment) }, onHandleIntent)
        },
    )
    Text(
        text = comment.content,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onPrimary,
    )
}
