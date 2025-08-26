package com.oiid.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.oiid.core.designsystem.composable.LinearProgress
import com.oiid.core.designsystem.icon.AppIcons
import oiid.core.base.designsystem.theme.OiidTheme

@Composable
internal fun SettingItem(
    title: String,
    onClick: () -> Unit,
    loading: Boolean = false,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    leadingIconContentDescription: String? = null,
    trailingIcon: ImageVector? = AppIcons.ArrowRight,
    trailingIconContentDescription: String? = null,
) {
    ElevatedCard(
        shape = RoundedCornerShape(OiidTheme.spacing.md),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dp.Hairline,
        ),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.heightIn(min = 64.dp)
                .clickable(
                    onClick = {
                        if (!loading) {
                            onClick()
                        }
                    },
                )
                .padding(
                    vertical = OiidTheme.spacing.sm,
                    horizontal = OiidTheme.spacing.lg,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = leadingIconContentDescription,
                    modifier = Modifier
                        .padding(end = OiidTheme.spacing.lg)
                        .clip(shape = RoundedCornerShape(56.dp)),
                )
            }

            Text(
                text = title,
                modifier = Modifier.weight(1f),
            )

            if (trailingIcon != null) {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = trailingIconContentDescription,
                )
            }
        }

        if (loading) {
            LinearProgress()
        }
    }
}
