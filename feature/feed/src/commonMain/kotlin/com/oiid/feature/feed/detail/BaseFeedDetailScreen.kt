package com.oiid.feature.feed.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.oiid.core.designsystem.composable.InfoTextPanel
import com.oiid.core.model.Profile
import oiid.core.base.designsystem.component.OiidTopAppBar
import oiid.core.base.designsystem.core.OiidTopAppBarConfiguration
import oiid.core.base.designsystem.core.TopAppBarVariant
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.ui.PostIntent
import oiid.core.ui.appBarHeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreenContent(
    modifier: Modifier = Modifier,
    networkStatus: PostCommentUiState,
    postUiState: PostDetailUiState,
    currentUserProfile: Profile?,
    title: String,
    onHandleIntent: (PostIntent) -> Unit,
    onBackClick: () -> Unit,
) {
    val isForum = postUiState.isForum
    val topPaddingValues = PaddingValues(top = if (isForum) appBarHeight() else 0.dp)

    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            val postItem = postUiState.postItem
            val error = postUiState.error

            if (error != null || postItem == null) {
                InfoTextPanel(message = error)
            } else {
                Box {
                    if (networkStatus.isLoading) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(topPaddingValues),)
                    }

                    PostDetailList(
                        modifier = modifier.padding(topPaddingValues),
                        post = postItem,
                        isForum = isForum,
                        comments = postUiState.comments,
                        currentUserProfile = currentUserProfile,
                        isLoading = postUiState.isLoading,
                        isPostingComment = networkStatus.isLoading,
                        onHandleIntent = onHandleIntent,
                    )

                    OiidTopAppBar(
                        configuration = OiidTopAppBarConfiguration(
                            title = if (isForum) title else "",
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
                                .copy(containerColor = if (isForum) colorScheme.background else Color.Transparent),
                            navigationIcon = Icons.Filled.ChevronLeft,
                            onNavigationIonClick = onBackClick,
                            variant = TopAppBarVariant.Small,
                        ),
                    )
                }
            }
        }
    }

}
