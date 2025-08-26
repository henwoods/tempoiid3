package com.oiid.core.designsystem.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oiid.core.designsystem.diagonalCornerShape
import oiid.core.base.designsystem.theme.OiidTheme
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.base.designsystem.theme.OiidTheme.typography

@Composable
fun AuthButton(modifier: Modifier = Modifier, text: String, enabled: Boolean, onClick: () -> Unit) {
    Button(
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorScheme.onSurface,
            contentColor = colorScheme.surface,
        ),
        enabled = enabled,
        shape = RoundedCornerShape(size = OiidTheme.spacing.sm),
        onClick = onClick,
        content = { Text(text = text.uppercase(), style = typography.bodyLarge.copy(fontSize = 20.sp)) },
    )
}

@Composable
fun AuthTextButton(modifier: Modifier = Modifier, text: String, enabled: Boolean, onClick: () -> Unit) {
    AuthTextButton(
        modifier = modifier,
        content = {
            Text(
                text = text,
                textDecoration = TextDecoration.Underline,
                style = typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
            )
        },
        enabled = enabled,
        onClick = onClick,
    )
}

@Composable
fun AuthTextButton(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier.padding(horizontal = OiidTheme.spacing.sm),
        colors = ButtonDefaults.textButtonColors().copy(
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        onClick = {
            if (enabled) {
                onClick()
            }
        },
        content = { content() },
    )
}

@Composable
fun OiidIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    color: Color = colorScheme.secondary,
    contentDescription: String? = null,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = color,
            modifier = Modifier.size(20.dp),
        )
    }
}


@Composable
fun OiidEventButton(modifier: Modifier = Modifier, text: String, isEnabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = isEnabled,
        shape = RoundedCornerShape(size = OiidTheme.spacing.sm),
    ) {
        Text(
            text = text,
            style = typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}