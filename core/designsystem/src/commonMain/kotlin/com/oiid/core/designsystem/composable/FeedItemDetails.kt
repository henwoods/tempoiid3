package com.oiid.core.designsystem.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.composeunstyled.Text
import oiid.core.base.designsystem.theme.OiidTheme

@Composable
fun FeedItemDetails(
    modifier: Modifier = Modifier,
    name: String,
    createdAt: String,
    avatar: (@Composable () -> Unit)? = null,
    actions: @Composable () -> Unit,
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(OiidTheme.spacing.sm),
            verticalAlignment = Alignment.Bottom,
        ) {
            if (avatar != null) {
                avatar()
            }
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Text(
                text = createdAt,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Light),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }

        actions()
    }
}