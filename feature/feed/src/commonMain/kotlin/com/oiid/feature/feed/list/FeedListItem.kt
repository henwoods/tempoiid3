package com.oiid.feature.feed.list

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oiid.core.designsystem.diagonalCornerShape
import com.oiid.core.model.PostItem
import com.oiid.feature.feed.composables.PostCard

data class FeedListItemUiState(val post: PostItem, val isPlaying: Boolean, val isForum: Boolean, val isDetail: Boolean)

@Composable
fun FeedListItem(
    modifier: Modifier = Modifier,
    uiState: FeedListItemUiState,
    onHandleIntent: (FeedIntent) -> Unit,
    initialPosition: Long = 0,
    onPositionChange: ((position: Long) -> Unit)? = null,
    onEnterFullscreen: ((String?) -> Unit)? = null,
) {
    PostCard(
        onCardClick = {
            onHandleIntent(FeedIntent.ItemSelected(uiState.post))
        },
        shape = diagonalCornerShape(),
        content = {
            Column {
                FeedContent(
                    modifier = modifier,
                    uiState = uiState,
                    initialPosition = initialPosition,
                    onPositionChange = onPositionChange,
                    onEnterFullscreen = onEnterFullscreen,
                    onHandleIntent = onHandleIntent,
                )

                FeedItemActions(
                    uiState.post,
                    {
                        onHandleIntent(FeedIntent.LikePost(uiState.post.id))
                    },
                    {
                        onHandleIntent(
                            FeedIntent.ItemSelected(
                                uiState.post,
                            ),
                        )
                    },
                )
            }
        },
    )
}
