package com.oiid.feature.feed.comment

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oiid.core.config.oiidTheme
import com.oiid.core.designsystem.diagonalCornerShape
import com.oiid.core.designsystem.theme.OiidTheme
import com.oiid.core.designsystem.theme.oiidDarkGray
import com.oiid.core.designsystem.theme.oiidGray
import oiid.core.base.designsystem.theme.OiidTheme.spacing

@Composable
fun CommentCard(modifier: Modifier = Modifier, isReply: Boolean = false, content: @Composable () -> Unit) {
    // The comment cards should always use the dark theme.
    OiidTheme(oiidColorScheme = oiidTheme(), true) {
        Card(
            modifier = modifier.fillMaxWidth().padding(horizontal = spacing.md),
            shape = diagonalCornerShape(),
            colors = CardDefaults.cardColors()
                .copy(
                    containerColor = if (isReply) {
                        oiidDarkGray
                    } else {
                        oiidGray
                    },
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            content()
        }
    }
}
