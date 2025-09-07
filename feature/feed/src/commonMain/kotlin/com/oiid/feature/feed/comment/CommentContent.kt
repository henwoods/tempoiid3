package com.oiid.feature.feed.comment

import androidx.compose.runtime.Composable
import com.composeunstyled.Text
import com.oiid.core.common.formatRelativeTime
import com.oiid.core.designsystem.composable.FeedItemDetails
import com.oiid.core.model.PostComment
import com.oiid.core.model.PostItem
import com.oiid.feature.feed.CommentActionsPopup
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.base.designsystem.theme.OiidTheme.typography
import oiid.core.ui.PostIntent

@Composable
fun CommentContent(
    comment: PostComment,
    post: PostItem,
    onStartReplying: (comment: PostComment) -> Unit,
    onHandleIntent: (PostIntent) -> Unit,
) {
    val badgeType = getCommentTagType(comment, post)

    FeedItemDetails(
        name = comment.name,
        createdAt = formatRelativeTime(comment.createdAt),
        isForum = false,
        actions = {
            CommentActionsPopup(comment.commentId, { onStartReplying(comment) }, onHandleIntent)
        },
        badges = {
            if (badgeType != null) {
                CommentBadge(type = badgeType)
            }
        },
    )
    Text(
        text = comment.content,
        style = typography.bodyLarge,
        color = colorScheme.onPrimary,
    )
}
