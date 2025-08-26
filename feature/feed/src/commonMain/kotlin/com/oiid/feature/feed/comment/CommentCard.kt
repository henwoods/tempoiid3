package com.oiid.feature.feed.comment

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oiid.core.designsystem.diagonalCornerShape
import oiid.core.base.designsystem.theme.OiidTheme

@Composable
fun CommentCard(modifier: Modifier = Modifier, isReply: Boolean = false, content: @Composable () -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth().padding(horizontal = OiidTheme.spacing.md),
        shape = diagonalCornerShape(),
        colors = CardDefaults.cardColors()
            .copy(
                containerColor = if (isReply) {
                    OiidTheme.colorScheme.surface
                } else {
                    OiidTheme.colorScheme
                        .surfaceContainerLowest
                },
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        content()
    }
}
