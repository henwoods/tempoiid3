package com.oiid.feature.fanzone.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.oiid.core.designsystem.composable.LinearProgress
import com.oiid.core.designsystem.composable.OiidButton
import com.oiid.core.designsystem.composable.OiidHeader
import com.oiid.core.designsystem.composable.OiidIconButton
import com.oiid.core.model.PostItem
import oiid.core.base.designsystem.theme.OiidTheme.colorScheme
import oiid.core.base.designsystem.theme.OiidTheme.spacing
import oiid.core.ui.FeedIntent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostBottomSheetDestination(
    bottomSheetState: SheetState,
    isLoading: Boolean = false,
    editingPost: PostItem? = null,
    onDismiss: () -> Unit,
    onHandleIntent: (FeedIntent) -> Unit,
) {
    ModalBottomSheet(
        modifier = Modifier.padding(top = spacing.xxl),
        dragHandle = {},
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
        onDismissRequest = {
            onDismiss()
        },
        sheetState = bottomSheetState,
        content = {
            CreatePostBottomSheetContent(
                modifier = Modifier.fillMaxHeight(.9f),
                isLoading = isLoading,
                editingPost = editingPost,
                onDismiss = onDismiss,
                onHandleIntent = onHandleIntent,
            )
        },
    )
}

@Composable
private fun CreatePostBottomSheetContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    editingPost: PostItem? = null,
    onDismiss: () -> Unit,
    onHandleIntent: (FeedIntent) -> Unit,
) {
    var title by remember { mutableStateOf(editingPost?.title ?: "") }
    var content by remember { mutableStateOf(editingPost?.content ?: "") }
    val isValid = title.isNotBlank() && content.isNotBlank()

    Column(
        modifier = modifier.background(colorScheme.surface).fillMaxWidth().padding(spacing.lg).navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OiidHeader(title = if (editingPost != null) "Edit Discussion" else "New Discussion")

            OiidIconButton(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                color = colorScheme.onPrimary,
                onClick = onDismiss,
            )
        }

        if (isLoading) {
            LinearProgress(modifier = Modifier.fillMaxWidth())
        }

        CreatePostText(
            text = title,
            label = "Discussion title",
            onValueChange = { title = it },
            singleLine = true,
            maxLength = 60,
            showCharacterCount = true,
        )

        CreatePostText(
            text = content, label = "Content", onValueChange = { content = it }, minLines = 5,
            modifier = Modifier.animateContentSize(),
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.sm),
        ) {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
            ) {
                Text(text = "Cancel", color = colorScheme.onPrimary)
            }

            OiidButton(
                text = if (editingPost != null) "Update" else "Post",
                enabled = isValid,
                onClick = {
                    if (isValid) {
                        onHandleIntent(FeedIntent.SavePost(title.trim(), content.trim()))
                    }
                },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
fun CreatePostText(
    modifier: Modifier = Modifier,
    text: String, label: String,
    singleLine: Boolean = false,
    minLines: Int = 1,
    maxLength: Int? = null,
    showCharacterCount: Boolean = false,
    onValueChange: (String) -> Unit,
) {
    val displayLabel = if (showCharacterCount && maxLength != null && text.isNotEmpty()) {
        "$label (${text.length}/$maxLength)"
    } else {
        label
    }

    TextField(
        value = text,
        shape = RoundedCornerShape(spacing.sm),
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = colorScheme.surfaceContainer,
            unfocusedContainerColor = colorScheme.surfaceContainer,
            focusedLabelColor = colorScheme.onPrimary,
            unfocusedIndicatorColor = colorScheme.surfaceContainer,
            disabledIndicatorColor = colorScheme.surfaceContainer,
            focusedIndicatorColor = colorScheme.surfaceContainer,
            cursorColor = colorScheme.onPrimary,
        ),
        onValueChange = { newValue ->
            if (maxLength != null && newValue.length <= maxLength) {
                onValueChange(newValue)
            } else if (maxLength == null) {
                onValueChange(newValue)
            }
        },
        label = { Text(displayLabel) },
        modifier = modifier.fillMaxWidth(),
        minLines = minLines,
        singleLine = singleLine,
    )
}
