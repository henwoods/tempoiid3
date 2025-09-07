package com.oiid.feature.feed.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.oiid.core.designsystem.diagonalCornerShape
import com.oiid.feature.feed.list.FeedListItemUiState
import oiid.core.base.designsystem.theme.OiidTheme
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.ui.FeedIntent

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    uiState: FeedListItemUiState,
    shape: Shape = diagonalCornerShape(),
    onHandleIntent: (FeedIntent) -> Unit,
    content: @Composable () -> Unit,
    actionItems: @Composable (() -> Unit)? = null,
) {
    Card(
        shape = shape,
        modifier = modifier.fillMaxWidth().padding(horizontal = 0.dp).shadow(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors().copy(containerColor = colorScheme.surface),
        onClick = {
            onHandleIntent(FeedIntent.ItemSelected(uiState.post))
        },
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize()) {
                content()
            }
            if (actionItems != null) {
                Box(modifier = Modifier.align(Alignment.TopStart).fillMaxWidth().padding(OiidTheme.spacing.sm)) {
                    actionItems()
                }
            }
        }
    }
}
