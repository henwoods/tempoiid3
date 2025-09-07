package com.oiid.core.designsystem.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.oiid.core.designsystem.appBarHeight

@Composable
fun FeedPanel(
    modifier: Modifier = Modifier,
    loadState: LoadStateResult,
    paddingValues: PaddingValues,
    appBar: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(modifier.padding(paddingValues).fillMaxSize()) {
        Column(Modifier.heightIn(max = appBarHeight())) {
            appBar()
        }

        if (loadState.isRefreshing) {
            LinearProgress(modifier = Modifier.fillMaxWidth())
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = if (loadState.showProgress) Arrangement.Center else Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            content()
        }
    }
}
