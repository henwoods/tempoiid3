package com.oiid.feature.feed.comment

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oiid.core.designsystem.composable.Tag
import com.oiid.core.designsystem.composable.TagIconLabel
import com.oiid.core.designsystem.composable.TagTextLabel
import com.oiid.core.model.PostComment
import com.oiid.core.model.PostItem
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.base.designsystem.theme.OiidTheme.spacing

enum class CommentTagType {
    BandAffiliate, PostAuthor, Superfan
}

@Composable
fun CommentBadge(
    modifier: Modifier = Modifier,
    type: CommentTagType,
) {
    when (type) {
        CommentTagType.BandAffiliate -> Tag(
            modifier = modifier,
            backgroundBrush = colorScheme.gradients.gradient,
            contentPadding = PaddingValues(horizontal = spacing.sm, vertical = spacing.xxs),
        ) {
            TagIconLabel(imageVector = Icons.Default.Star, contentDescription = "Band Affiliate")
        }

        CommentTagType.PostAuthor -> Tag {
            TagTextLabel(text = "Post author")
        }

        CommentTagType.Superfan -> Tag(backgroundBrush = colorScheme.gradients.gradient) {
            TagTextLabel(text = "SUPERFAN", color = colorScheme.onPrimary)
        }
    }
}

fun getCommentTagType(comment: PostComment, post: PostItem): CommentTagType? {
    return when {
        comment.isBandAffiliate -> CommentTagType.BandAffiliate
        comment.userId == post.userId -> CommentTagType.PostAuthor
        comment.isSuperfan -> CommentTagType.Superfan
        else -> null
    }
}

fun getPostTagType(post: PostItem): CommentTagType? {
    return when {
        post.isBandAffiliate -> CommentTagType.BandAffiliate
        post.isSuperfan -> CommentTagType.Superfan
        else -> null
    }
}