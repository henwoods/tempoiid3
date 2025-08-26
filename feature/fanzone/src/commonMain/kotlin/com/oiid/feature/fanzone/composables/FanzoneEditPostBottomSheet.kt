package com.oiid.feature.fanzone.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.oiid.core.model.PostItem
import kotlinx.coroutines.launch
import oiid.core.ui.FeedIntent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FanzoneEditPostBottomSheet(
    showDialog: Boolean,
    editingPost: PostItem?,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onHandleIntent: (FeedIntent) -> Unit,
) {
    if (showDialog) {
        val coroutineScope = rememberCoroutineScope()
        val bottomSheetState = rememberModalBottomSheetState(true)

        LaunchedEffect(bottomSheetState) {
            bottomSheetState.hide()
        }

        CreatePostBottomSheetDestination(
            bottomSheetState = bottomSheetState,
            editingPost = editingPost,
            onDismiss = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                    onDismiss()
                }
            },
            isLoading = isLoading,
            onHandleIntent = onHandleIntent,
        )
    }
}