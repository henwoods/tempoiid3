package com.oiid.feature.fanzone

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oiid.core.designsystem.composable.ConfirmationDialog
import com.oiid.feature.fanzone.composables.FanzoneEditPostBottomSheet
import com.oiid.feature.fanzone.list.FanzoneFeedViewModel
import com.oiid.feature.feed.BaseFeedScreen
import kotlinx.coroutines.launch
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.ui.UiEventHandler
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun FanzoneScreen(
    modifier: Modifier,
    viewModel: FanzoneFeedViewModel = koinViewModel(),
    onPostClicked: (String) -> Unit,
    onNavigateToEditProfile: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedItem by viewModel.selectedItem.collectAsStateWithLifecycle()
    val isPostingComment = viewModel.postingCommentState.collectAsState().value.isLoading
    val createPostModal = viewModel.showCreatePostDialog.collectAsState(false)
    val editingPost by viewModel.editingPost.collectAsStateWithLifecycle()
    val showMissingNameDialog by viewModel.showMissingNameDialog.collectAsStateWithLifecycle()

    UiEventHandler(uiEventFlow = viewModel.uiEvent)

    BaseFeedScreen(
        modifier = modifier.fillMaxSize(),
        appBar = {
            FanzoneScreenAppBar {
                coroutineScope.launch {
                    viewModel.showCreatePostDialog()
                }
            }
        },
        listBackgroundColor = colorScheme.surface,
        uiState = uiState,
        selectedItem = selectedItem,
        setHasNavigated = viewModel::setHasNavigated,
        onPostClicked = onPostClicked,
        divider = {
            HorizontalDivider(color = colorScheme.outline, thickness = Dp.Hairline)
        },
        onHandleIntent = viewModel::handleIntent,
    )

    FanzoneEditPostBottomSheet(
        showDialog = createPostModal.value,
        editingPost = editingPost,
        isLoading = isPostingComment,
        onDismiss = viewModel::hideCreatePostDialog,
        onHandleIntent = viewModel::handleIntent,
    )
    
    ConfirmationDialog(
        title = "Missing Profile Name",
        message = "Hey there! To join a discussion, you need to set a name on your profile.",
        confirmButtonText = "On it!",
        onConfirm = {
            onNavigateToEditProfile()
        },
        onDismiss = viewModel::hideMissingNameDialog,
        isVisible = showMissingNameDialog,
    )
}
