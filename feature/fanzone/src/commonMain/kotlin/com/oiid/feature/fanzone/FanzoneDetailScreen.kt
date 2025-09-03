package com.oiid.feature.fanzone

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.oiid.feature.fanzone.composables.FanzoneEditPostBottomSheet
import com.oiid.feature.fanzone.detail.FanzonePostPostDetailViewModel
import com.oiid.feature.feed.detail.PostDetailScreenContent
import com.oiid.feature.feed.detail.PostCommentUiState
import com.oiid.feature.feed.detail.PostDetailUiState
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.ui.FeedIntent
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun FanzoneDetailScreen(
    modifier: Modifier = Modifier,
    postId: String,
    artistId: String,
    onBackClick: () -> Unit,
    viewModel: FanzonePostPostDetailViewModel = koinViewModel(key = "${artistId}_$postId") {
        parametersOf(postId, artistId)
    },
) {
    val postCommentState = viewModel.postingCommentState.collectAsState()
    val postUiState = viewModel.post.collectAsState(PostDetailUiState(isForum = true))
    val currentUserProfile = viewModel.currentUserProfile.collectAsState()
    val editingPost = viewModel.editingPost.collectAsState()
    val showCreatePostDialog = viewModel.showCreatePostDialog.collectAsState()

    PostDetailScreenContent(
        modifier = modifier.background(colorScheme.background),
        networkStatus = if (!showCreatePostDialog.value) postCommentState.value else PostCommentUiState(),
        postUiState = postUiState.value,
        currentUserProfile = currentUserProfile.value,
        title = "Fan-zone",
        onHandleIntent = viewModel::handleIntent,
        onBackClick = onBackClick,
    )

    FanzoneEditPostBottomSheet(
        showDialog = showCreatePostDialog.value,
        editingPost = editingPost.value,
        isLoading = postCommentState.value.isLoading,
        onDismiss = viewModel::hideCreatePostDialog,
        onHandleIntent = { intent ->
            when (intent) {
                is FeedIntent.SavePost -> {
                    viewModel.updatePost(intent.title, intent.content)
                }

                else -> {

                }
            }
        },
    )
}
