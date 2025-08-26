package com.oiid.feature.feed.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oiid.core.designsystem.diagonalCornerShape
import com.oiid.feature.feed.composables.PostCard
import com.oiid.feature.feed.list.FeedContent
import com.oiid.feature.feed.list.FeedItemActions
import com.oiid.feature.feed.list.FeedListItemUiState
import oiid.core.ui.FeedIntent

@Composable
fun PostItemCard(
    uiState: FeedListItemUiState,
    onHandleIntent: (FeedIntent) -> Unit,
) {
    val post = uiState.post

    PostCard(
        uiState = uiState,
        onHandleIntent = {},
        shape = diagonalCornerShape(),
        content = {
            Column {
                FeedContent(
                    modifier = Modifier,
                    uiState = uiState,
                    onHandleIntent = onHandleIntent,
                    maxLines = Int.MAX_VALUE,
                )
                FeedItemActions(
                    post = post,
                    onHandleIntent = onHandleIntent,
                )
            }
        },
    )
}