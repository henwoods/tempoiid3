package com.oiid.feature.feed.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.oiid.core.common.formatRelativeTime
import com.oiid.core.designsystem.Dimens.Companion.POST_MEDIA_HEIGHT
import com.oiid.core.designsystem.composable.FeedItemDetails
import com.oiid.core.designsystem.composable.OiidBodyText
import com.oiid.core.designsystem.composable.OiidHeader
import com.oiid.core.designsystem.composable.UserAvatar
import com.oiid.core.designsystem.composable.UserAvaterType
import com.oiid.core.designsystem.diagonalCornerShape
import com.oiid.feature.feed.FeedPostActionsPopup
import com.oiid.feature.feed.UserPostActionsPopup
import com.oiid.feature.feed.comment.CommentBadge
import com.oiid.feature.feed.comment.getPostTagType
import com.oiid.feature.player.video.VideoPlayer
import oiid.core.base.designsystem.theme.OiidTheme
import oiid.core.base.designsystem.theme.OiidTheme.spacing
import oiid.core.ui.FeedIntent

@Composable
fun FeedContent(
    modifier: Modifier,
    uiState: FeedListItemUiState,
    maxLines: Int = 3,
    initialPosition: Long = 0,
    onHandleIntent: (FeedIntent) -> Unit,
    onPositionChange: ((position: Long) -> Unit)? = null,
    onEnterFullscreen: ((String?) -> Unit)? = null,
) {
    val post = uiState.post

    Column {
        if (uiState.isForum) {
            val postBadgeType = getPostTagType(post)
            FeedItemDetails(
                avatar = {
                    UserAvatar(type = UserAvaterType.Tertiary, imageUrl = post.profileImage)
                },
                modifier = Modifier.padding(top = spacing.md, start = spacing.md, end = spacing.md),
                name = post.name,
                createdAt = formatRelativeTime(post.createdAt),
                isForum = uiState.isForum,
                badges = {
                    if (postBadgeType != null) {
                        CommentBadge(type = postBadgeType)
                    }
                },
                actions = {
                    if (post.isPostByUser) {
                        UserPostActionsPopup(post.id, onHandleIntent)
                    } else {
                        FeedPostActionsPopup(post.id, onHandleIntent)
                    }
                },
            )
        }

        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(OiidTheme.spacing.md)) {
            val isMedia = post.imageURLs.isNotEmpty()
            if (isMedia) {
                val mediaUrl = post.imageURLs.first()
                if (mediaUrl.endsWith(".mp4")) {
                    Column(modifier = modifier.height(POST_MEDIA_HEIGHT).clip(diagonalCornerShape())) {
                        VideoPlayer(
                            modifier = modifier,
                            videoUrl = post.imageURLs.first(),
                            isPlaying = uiState.isPlaying,
                            initialPosition = initialPosition,
                            onPositionChange = onPositionChange,
                            onEnterFullscreen = onEnterFullscreen,
                        )
                    }
                } else {
                    ImageContent(modifier = Modifier.clip(diagonalCornerShape()), imageUrls = post.imageURLs)
                }
            }

            if (post.title.isNotBlank()) {
                Column(
                    modifier = Modifier.padding(horizontal = OiidTheme.spacing.md),
                    verticalArrangement =
                        Arrangement.spacedBy(OiidTheme.spacing.xs),
                ) {
                    if (!isMedia) {
                        Spacer(modifier = Modifier.height(OiidTheme.spacing.md))
                    }

                    OiidHeader(title = post.title)

                    if (post.content.isNotBlank()) {
                        OiidBodyText(text = post.content, maxLines = maxLines)
                    }
                }
            }

            if (!uiState.isForum) {
                HorizontalDivider(color = DividerDefaults.color.copy(alpha = 0.3f))
            }
        }
    }
}
