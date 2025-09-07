package com.oiid.feature.feed

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Reply
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.runtime.Composable
import com.oiid.core.designsystem.composable.DropdownOption
import com.oiid.core.designsystem.composable.OPTION_KEY_EDIT
import com.oiid.core.designsystem.composable.OPTION_KEY_REPLY
import com.oiid.core.designsystem.composable.OPTION_KEY_REPORT
import com.oiid.core.designsystem.composable.PopupMenu
import oiid.core.ui.FeedIntent
import oiid.core.ui.PostIntent

@Composable
fun CommentActionsPopup(id: String, onStartReplying: () -> Unit, onHandleIntent: (PostIntent) -> Unit) {
    PopupMenu(
        options = getCommentOptions(),
        onOptionClick = { optionKey ->
            when (optionKey) {
                OPTION_KEY_REPORT -> {
                    onHandleIntent(PostIntent.ReportComment(id))
                }

                OPTION_KEY_REPLY -> {
                    onStartReplying()
                }
            }
        },
    )
}

fun getCommentOptions(): List<DropdownOption> {
    return listOf(
        DropdownOption(OPTION_KEY_REPORT, "Report comment", Icons.Outlined.Flag),
        DropdownOption(OPTION_KEY_REPLY, "Reply to comment", Icons.AutoMirrored.Outlined.Reply),
    )
}

@Composable
fun UserPostActionsPopup(id: String, onHandleIntent: (FeedIntent) -> Unit) {
    val options = listOf(DropdownOption(OPTION_KEY_EDIT, "Edit post", Icons.Outlined.EditNote))

    PopupMenu(
        options = options,
        onOptionClick = { optionKey ->
            when (optionKey) {
                OPTION_KEY_EDIT -> {
                    onHandleIntent(FeedIntent.EditPost(id))
                }
            }
        },
    )
}

@Composable
fun FeedPostActionsPopup(id: String, onHandleIntent: (FeedIntent) -> Unit) {
    val options = listOf(DropdownOption(OPTION_KEY_REPORT, "Report post", Icons.Outlined.Flag))

    PopupMenu(
        options = options,
        onOptionClick = { optionKey ->
            when (optionKey) {
                OPTION_KEY_REPORT -> {
                    onHandleIntent(FeedIntent.ReportPost(id))
                }
            }
        },
    )
}
