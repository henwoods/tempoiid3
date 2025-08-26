package com.oiid.feature.feed.list

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.ViewModel
import com.oiid.core.designsystem.diagonalCornerShape
import com.oiid.core.designsystem.ext.bottomNavPadding
import com.oiid.core.model.PostType
import com.oiid.core.model.postType
import com.oiid.feature.player.video.FullscreenVideoPlayer
import kotlin.math.abs
import kotlinx.coroutines.delay
import oiid.core.base.designsystem.theme.OiidTheme.spacing
import oiid.core.ui.FeedIntent
import org.koin.compose.viewmodel.koinViewModel

class FeedListViewModel : ViewModel() {
    var scrollIndex: Int = 0
    var scrollOffset: Int = 0

    val videoPlaybackPositions = mutableMapOf<String, Long>()
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FeedList(
    modifier: Modifier = Modifier,
    uiState: FeedUiState,
    divider: (@Composable () -> Unit)?,
    onHandleIntent: (FeedIntent) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: FeedListViewModel = koinViewModel(),
) {
    val listState = rememberLazyListState()
    var activeFeedId by remember { mutableStateOf<String?>(null) }
    var fullscreenVideo by rememberSaveable { mutableStateOf<String?>(null) }
    var fullscreenVideoId by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        listState.scrollToItem(viewModel.scrollIndex, viewModel.scrollOffset)
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collect { (index, offset) ->
            viewModel.scrollIndex = index
            viewModel.scrollOffset = offset
        }
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            delay(500)
            if (!listState.isScrollInProgress) {
                val centermostItem = listState.layoutInfo.visibleItemsInfo.minByOrNull {
                    val viewportCenter = listState.layoutInfo.viewportSize.height / 2
                    val itemCenter = it.offset + it.size / 2
                    abs(viewportCenter - itemCenter)
                }
                activeFeedId = centermostItem?.key as? String
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (fullscreenVideo == null) {
            LazyColumn(
                state = listState,
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(spacing.md),
                contentPadding = bottomNavPadding(),
            ) {
                itemsIndexed(
                    items = uiState.posts,
                    key = { index, item -> item.id },
                    contentType = { index, item -> item.postType },
                ) { index, entity ->

                    val isPlaying = entity.id == activeFeedId

                    // Shared element transitions with videos are janky, will need time to resolve animation issues with that.
                    // Also disable shared transitions for forum posts
                    if (entity.postType != PostType.VIDEO && !uiState.isForum) {
                        with(sharedTransitionScope) {
                            FeedListItem(
                                modifier = Modifier.sharedElement(
                                    sharedContentState = rememberSharedContentState("item-${entity.id}"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                ).clip(diagonalCornerShape()),
                                uiState = FeedListItemUiState(entity, isPlaying, uiState.isForum,
                                    isDetail = false),
                                onHandleIntent = onHandleIntent,
                            )
                        }
                    } else {
                        FeedListItem(
                            uiState = FeedListItemUiState(entity, isPlaying, uiState.isForum,
                                isDetail = false),
                            onHandleIntent = onHandleIntent,
                            initialPosition = viewModel.videoPlaybackPositions[entity.id] ?: 0L,
                            onPositionChange = { newPosition ->
                                viewModel.videoPlaybackPositions[entity.id] = newPosition
                            },
                            onEnterFullscreen = {
                                fullscreenVideoId = entity.id
                                fullscreenVideo = it
                            },
                        )
                    }

                    if (index < uiState.posts.lastIndex && divider != null)
                        divider()
                }
            }
        }

        fullscreenVideo?.let { item ->
            FullscreenVideoPlayer(
                id = fullscreenVideoId ?: "",
                url = fullscreenVideo ?: "",
                onDismiss = {
                    fullscreenVideo = null
                },
                onPositionChange = { videoId, position ->
                    viewModel.videoPlaybackPositions[videoId] = position
                },
                getInitialPosition = { videoId ->
                    viewModel.videoPlaybackPositions[videoId] ?: 0L
                },
            )
        }
    }
}
