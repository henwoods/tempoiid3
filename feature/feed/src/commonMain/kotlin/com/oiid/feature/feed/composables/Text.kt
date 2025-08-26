package com.oiid.feature.feed.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun FeedItemTitle(title: String) {
    Text(text = title, style = MaterialTheme.typography.headlineSmall)
}

@Composable
fun FeedItemBody(body: String, maxLines: Int = 3) {
    Text(
        text = body,
        style = MaterialTheme.typography.bodyLarge,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}