package com.oiid.feature.feed.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.oiid.core.designsystem.composable.InfoTextPanel
import com.oiid.core.model.PostItem
import com.oiid.feature.feed.list.PostIntent
import oiid.core.base.designsystem.component.OiidTopAppBar
import oiid.core.base.designsystem.core.OiidTopAppBarConfiguration
import oiid.core.base.designsystem.core.TopAppBarVariant
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseFeedPostDetailScreen(
    modifier: Modifier = Modifier,
    networkStatus: PostCommentUiState,
    postUiState: PostDetailUiState,
    isForum: Boolean,
    title: String,
    onHandleIntent: (PostIntent) -> Unit,
    onBackClick: () -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            val postItem = postUiState.postItem
            val error = postUiState.error

            if (error != null || postItem == null) {
                InfoTextPanel(message = error)
            } else {
                Box {
                    if (networkStatus.isLoading) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(top = if (isForum) 56.dp else 0.dp))
                    }

                    PostDetailContent(
                        modifier = modifier.padding(top = if (isForum) 56.dp else 0.dp),
                        post = postItem,
                        isForum = isForum,
                        comments = postUiState.comments,
                        isLoading = postUiState.isLoading,
                        onHandleIntent = onHandleIntent,
                    )

                    if (isForum) {
                        OiidTopAppBar(
                            configuration = OiidTopAppBarConfiguration(
                                title = title,
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
                                    .copy(containerColor = colorScheme.surfaceDim),
                                navigationIcon = Icons.Filled.ChevronLeft,
                                onNavigationIonClick = onBackClick,
                                variant = TopAppBarVariant.Small,
                            ),
                        )

                    } else {
                        OiidTopAppBar(
                            configuration = OiidTopAppBarConfiguration(
                                title = "",
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
                                    .copy(containerColor = Color.Transparent),
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

}
