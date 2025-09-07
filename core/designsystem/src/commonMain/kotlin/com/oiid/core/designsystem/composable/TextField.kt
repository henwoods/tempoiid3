package com.oiid.core.designsystem.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect.Companion.dashPathEffect
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.oiid.core.designsystem.icon.AppIcons
import oiid.core.base.designsystem.theme.OiidTheme
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.base.designsystem.theme.OiidTheme.spacing
import oiid.core.base.designsystem.theme.OiidTheme.typography

@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    enabled: Boolean,
    isPassword: Boolean = false,
    type: KeyboardType = KeyboardType.Email,
    onValueChange: (String) -> Unit,
    onDone: (() -> Unit)? = null,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var passwordVisible by remember { mutableStateOf(false) }
    var focused by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        BasicTextField(
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = type,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onDone?.invoke()
                },
            ),
            singleLine = true,
            value = value,
            enabled = enabled,
            onValueChange = onValueChange,
            textStyle = typography.bodyLarge,
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            modifier = Modifier.onFocusChanged(
                {
                    focused = it.isFocused
                },
            ).fillMaxWidth().clip(RoundedCornerShape(spacing.lg))
                .background(colorScheme.onSurface).border(
                    1.dp,
                    colorScheme.onSurface.copy(alpha = 0.4f),
                    RoundedCornerShape(spacing.lg),
                ).padding(horizontal = spacing.lg, vertical = spacing.md),
            decorationBox = { innerTextField ->
                VisibilityToggle(
                    isPassword = isPassword,
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityToggle = {
                        passwordVisible = !passwordVisible
                    },
                    placeholder = {
                        TextPlaceholder(focused, value, label, innerTextField)
                    },
                )
            },
        )
    }
}

@Composable
fun ColumnScope.TextPlaceholder(
    isFocused: Boolean,
    value: String,
    label: String,
    innerTextField: @Composable () -> Unit,
) {
    Box(modifier = Modifier.weight(1f)) {
        Column {
            AnimatedVisibility(!isFocused && value.isEmpty(), enter = fadeIn(), exit = fadeOut()) {
                Text(
                    text = label,
                    style = typography.bodyLarge,
                    color = colorScheme.surface,
                )
            }
        }
        innerTextField()
    }
}

@Composable
fun VisibilityToggle(
    isPassword: Boolean,
    passwordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit,
    placeholder: @Composable () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        placeholder()

        if (isPassword) {
            Spacer(modifier = Modifier.size(OiidTheme.spacing.sm))
            IconButton(
                onClick = { onPasswordVisibilityToggle() },
                modifier = Modifier.size(OiidTheme.spacing.lg),
            ) {
                Icon(
                    imageVector = if (passwordVisible) AppIcons.Visibility else AppIcons.VisibilityOff,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = colorScheme.surface,
                )
            }
        }
    }
}

@Composable
fun UnderlineTextField(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    label: String? = null,
    value: String,
    enabled: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onCancel: (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
) {
    val focused = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val composableLabelOrNull: @Composable (() -> Unit)? = if (!label.isNullOrEmpty()) {
        {
            if (focused.value || value.trim().isNotBlank()) {
                Text(text = label, modifier = modifier.alpha(.6f), style = OiidTheme.typography.bodyMedium)
            } else {
                Text(text = label, modifier = modifier.alpha(.8f))
            }
        }
    } else {
        null
    }

    val underlineColor = colorScheme.onBackground
    val innerModifier = if (focusRequester != null) {
        Modifier.focusRequester(focusRequester)
    } else {
        Modifier
    }

    Box(modifier = modifier) {
        OutlinedTextField(
            modifier = innerModifier.drawBehind {
                val strokeWidth = 1.dp.toPx()
                val width = size.width
                val y = (size.height - strokeWidth / 2) - 8.dp.toPx()
                drawLine(
                    color = underlineColor,
                    start = Offset(8.dp.toPx(), y),
                    end = Offset(width - 8.dp.toPx(), y),
                    strokeWidth = strokeWidth,
                    pathEffect = dashPathEffect(floatArrayOf(6f, 8f), 0f),
                )
            }.onFocusChanged { focusState ->
                focused.value = focusState.isFocused
            }.padding(PaddingValues()).imePadding(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                focusedTextColor = colorScheme.onBackground,
                unfocusedTextColor = colorScheme.onBackground,
                focusedLabelColor = colorScheme.onBackground,
                unfocusedLabelColor = colorScheme.onBackground,
            ),
            enabled = enabled,
            maxLines = maxLines,
            minLines = minLines,
            textStyle = textStyle,
            label = composableLabelOrNull,
            value = value,
            onValueChange = onValueChange,
        )

        if (onCancel != null) {
            AnimatedVisibility(
                visible = value.isNotBlank() || focused.value,
                modifier = Modifier.align(Alignment.TopEnd),
            ) {
                TextButton(
                    contentPadding = PaddingValues(horizontal = spacing.sm, vertical = 0.dp),
                    modifier = Modifier.height(12.dp),
                    content = {
                        Text(
                            "Cancel",
                            style = typography.labelMedium,
                            color = colorScheme.onSurface,
                        )
                    },
                    onClick = {
                        onValueChange("")
                        focusManager.clearFocus()
                        onCancel.invoke()
                    },
                )
            }
        }
    }
}
