package com.oiid.core.designsystem.composable

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.core.Separator
import com.composeunstyled.Button
import com.composeunstyled.DropdownMenu
import com.composeunstyled.DropdownMenuPanel
import com.composeunstyled.Icon
import com.composeunstyled.Text
import oiid.core.base.designsystem.theme.OiidTheme

const val OPTION_KEY_REPLY = "reply"
const val OPTION_KEY_LIKE_COMMENT = "like_comment"
const val OPTION_KEY_REPORT = "report"
const val OPTION_KEY_EDIT = "edit"

class DropdownOption(
    val key: String,
    val text: String,
    val icon: ImageVector,
    val enabled: Boolean = true,
    val dangerous: Boolean = false,
)

@Composable
fun PopupMenu(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopCenter,
    options: List<DropdownOption>,
    finalSeparatorOffset: Int = 1,
    size: Dp = 24.dp,
    onOptionClick: (optionKey: String) -> Unit,
) {
    Box(modifier = modifier, contentAlignment = alignment) {
        var expanded by remember { mutableStateOf(false) }

        DropdownMenu(onExpandRequest = { expanded = true }) {
            IconButton(
                modifier = Modifier.size(size),
                onClick = {
                    expanded = true
                },
                content = {
                    Icon(
                        tint = OiidTheme.colorScheme.onSurface,
                        imageVector = Icons.Outlined.MoreHoriz,
                        contentDescription = "More menu",
                    )
                },
            )

            val finalSeparatorPosition = (options.size - finalSeparatorOffset).coerceIn(0, options.lastIndex)

            DropdownMenuPanel(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                backgroundColor = OiidTheme.colorScheme.surface,
                shape = RoundedCornerShape(OiidTheme.spacing.sm),
                modifier = Modifier.padding(vertical = OiidTheme.spacing.xs).width(240.dp).shadow(
                    OiidTheme.spacing.xs,
                    RoundedCornerShape(OiidTheme.spacing.sm),
                ),
                enter = scaleIn(
                    animationSpec = tween(durationMillis = 120, easing = LinearOutSlowInEasing),
                    initialScale = 0.8f,
                    transformOrigin = TransformOrigin(0f, 0f),
                ) + fadeIn(tween(durationMillis = 30)),
                exit = scaleOut(
                    animationSpec = tween(durationMillis = 1, delayMillis = 75),
                    targetScale = 1f,
                ) + fadeOut(tween(durationMillis = 75)),
            ) {
                options.forEachIndexed { index, option ->
                    if (index == 1 || index == finalSeparatorPosition) {
                        Separator(color = Color(0xFFBDBDBD))
                    }

                    val contentColor =
                        (if (option.dangerous) Color(0xFFC62828) else OiidTheme.colorScheme.onSurface).copy(alpha = if (option.enabled) 1f else 0.5f)
                    Button(
                        onClick = {
                            onOptionClick(option.key)
                            expanded = false
                        },
                        enabled = option.enabled,
                        modifier = Modifier.padding(OiidTheme.spacing.xs),
                        contentPadding = PaddingValues(horizontal = OiidTheme.spacing.sm, vertical = 2.dp),
                        contentColor = contentColor,
                        shape = RoundedCornerShape(OiidTheme.spacing.sm),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = option.text,
                                style = OiidTheme.typography.bodyLarge,
                                color = contentColor,
                                modifier = Modifier.fillMaxWidth().weight(1f).padding(
                                    vertical = OiidTheme.spacing.sm,
                                    horizontal = OiidTheme.spacing.xs,
                                ),
                            )
                            Icon(
                                imageVector = option.icon,
                                contentDescription = "${option.text} icon",
                                tint = contentColor,
                            )
                        }
                    }
                }
            }
        }
    }
}
