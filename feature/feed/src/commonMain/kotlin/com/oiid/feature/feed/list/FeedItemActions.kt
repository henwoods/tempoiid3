package com.oiid.feature.feed.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.oiid.core.designsystem.generated.resources.Res
import com.oiid.core.designsystem.generated.resources.comment
import com.oiid.core.designsystem.generated.resources.like
import com.oiid.core.designsystem.generated.resources.liked
import com.oiid.core.model.PostItem
import oiid.core.base.designsystem.theme.OiidTheme
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.ui.FeedIntent
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun FeedItemActions(
    post: PostItem,
    onHandleIntent: (FeedIntent) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(
            horizontal = OiidTheme.spacing.lg,
            vertical = OiidTheme.spacing.md,
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        if (post.isPinned) {
            IconButton(modifier = Modifier.size(28.dp), onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.PushPin,
                    contentDescription = "Pin",
                )
            }
        } else {
            Spacer(modifier = Modifier.width(OiidTheme.spacing.md))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(OiidTheme.spacing.md)) {
            FeedItemTextAction(
                text = "${post.numberOfLikes} likes",
                onClick = {
                    onHandleIntent(FeedIntent.LikePost(post.id))
                },
                painter = painterResource(
                    if (post.isLikedByUser) Res.drawable.liked else Res.drawable.like,
                ),
                brush = if (post.isLikedByUser) colorScheme.gradients.buttonImageHighlightColor else null,
            )
            FeedItemTextAction(
                text = "${post.numberOfComments} comments",
                onClick = {
                    onHandleIntent(FeedIntent.ItemSelected(post))
                },
                bitmap = imageResource(Res.drawable.comment),
            )
        }
    }
}

@Composable
fun FeedItemTextAction(text: String, onClick: () -> Unit, imageVector: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(OiidTheme.spacing.sm),
    ) {
        IconButton(modifier = Modifier.size(28.dp), onClick = onClick) {
            Icon(imageVector = imageVector, contentDescription = text)
        }
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun FeedItemTextAction(
    text: String,
    onClick: () -> Unit,
    painter: Painter,
    brush: Brush? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(OiidTheme.spacing.sm),
    ) {
        IconButton(modifier = Modifier.size(28.dp), onClick = onClick) {
            Icon(
                painter = painter,
                contentDescription = text,
                tint = if (brush == null) MaterialTheme.colorScheme.onSurfaceVariant else Color.Unspecified,
                modifier = if (brush != null) {
                    // This modifier applies the gradient
                    Modifier
                        // Hack to ensure the blend works
                        .graphicsLayer(alpha = 0.99f)
                        .drawWithContent {
                            drawContent()
                            drawRect(
                                brush = brush,
                                blendMode = BlendMode.SrcIn,
                            )
                        }
                } else {
                    Modifier
                },
            )
        }
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun FeedItemTextAction(text: String, onClick: () -> Unit, bitmap: ImageBitmap) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(OiidTheme.spacing.sm),
    ) {
        IconButton(modifier = Modifier.size(28.dp), onClick = onClick) {
            Icon(bitmap = bitmap, contentDescription = text)
        }
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}
