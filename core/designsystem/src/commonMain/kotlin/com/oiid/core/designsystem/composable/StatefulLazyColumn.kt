package com.oiid.core.designsystem.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.oiid.core.designsystem.ext.bottomNavPadding
import oiid.core.base.designsystem.theme.OiidTheme.spacing

/**
 * A reusable LazyColumn that automatically preserves scroll state
 * and applies standard app styling (background, spacing, padding).
 *
 * @param modifier Modifier to be applied to the LazyColumn
 * @param scrollStateViewModel ViewModel that preserves scroll position
 * @param content The content to be displayed in the LazyColumn
 */
@Composable
fun StatefulLazyColumn(
    modifier: Modifier = Modifier,
    scrollStateViewModel: ScrollStateViewModel,
    contentPadding: PaddingValues = PaddingValues(),
    content: LazyListScope.() -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        listState.scrollToItem(scrollStateViewModel.scrollIndex, scrollStateViewModel.scrollOffset)
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collect { (index, offset) ->
            scrollStateViewModel.updateScrollPosition(index, offset)
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
        contentPadding = bottomNavPadding(contentPadding),
        content = content,
    )
}
