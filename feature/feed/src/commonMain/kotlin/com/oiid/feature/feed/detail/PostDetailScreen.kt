package com.oiid.feature.feed.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import oiid.core.ui.UiEventHandler
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PostDetailScreen(
    modifier: Modifier = Modifier,
    postId: String,
    artistId: String,
    isForum: Boolean,
    onBackClick: () -> Unit,
    viewModel: FeedPostDetailViewModel = koinViewModel(key = "${artistId}_$postId") {
        parametersOf(postId, artistId)
    },
) {
    val postCommentState = viewModel.postingCommentState.collectAsState()
    val post = viewModel.post.collectAsState(PostDetailUiState(isForum = isForum))
    val currentUserProfile = viewModel.currentUserProfile.collectAsState()

    UiEventHandler(viewModel.uiEvent)

    PostDetailScreenContent(
        modifier = modifier,
        networkStatus = postCommentState.value,
        postUiState = post.value,
        currentUserProfile = currentUserProfile.value,
        title = "Post",
        onHandleIntent = { viewModel.handleIntent(it) },
        onBackClick = onBackClick,
    )
}

