package com.oiid.feature.feed.list

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oiid.core.designsystem.diagonalCornerShape
import com.oiid.core.model.PostItem
import com.oiid.feature.feed.composables.PostCard
import oiid.core.ui.FeedIntent

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
        onHandleIntent = onHandleIntent,
        uiState = uiState,
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
                    post = uiState.post,
                    onHandleIntent = onHandleIntent,
                )
            }
        },
    )
}
