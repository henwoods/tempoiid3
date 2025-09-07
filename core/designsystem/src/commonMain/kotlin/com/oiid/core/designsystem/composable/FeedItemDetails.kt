package com.oiid.core.designsystem.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.composeunstyled.Text
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.base.designsystem.theme.OiidTheme.spacing
import oiid.core.base.designsystem.theme.OiidTheme.typography

@Composable
fun FeedItemDetails(
    modifier: Modifier = Modifier,
    name: String,
    createdAt: String,
    isForum: Boolean,
    avatar: (@Composable () -> Unit)? = null,
    badges: (@Composable () -> Unit)? = null,
    actions: @Composable () -> Unit,
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (avatar != null) {
                avatar()
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = name.ifEmpty { "No name" },
                    style = if (isForum) {
                        typography.bodyMedium
                    } else {
                        typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    },
                    color = colorScheme.onSurface,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = createdAt,
                        style = if (isForum) {
                            typography.bodyMedium.copy(
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Light,
                            )
                        } else {
                            typography.bodyLarge.copy(
                                fontWeight = FontWeight.Light,
                            )
                        },
                        color = colorScheme.onSurface,
                    )
                    if (badges != null) {
                        badges()
                    }
                }
            }
        }

        actions()
    }
}
