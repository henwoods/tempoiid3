package oiid.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.oiid.core.designsystem.composable.ConfirmationDialog
import com.oiid.core.designsystem.generated.resources.Res
import com.oiid.core.designsystem.generated.resources.dialog_generic_confirm_button
import com.oiid.core.designsystem.generated.resources.dialog_generic_dismiss_button
import com.oiid.core.designsystem.generated.resources.dialog_generic_ok_button
import com.oiid.core.designsystem.generated.resources.dialog_generic_validation_message
import com.oiid.core.designsystem.generated.resources.dialog_generic_validation_title
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Stable
class ConfirmationDialogState<T> internal constructor(
    private val onHandleIntent: (T) -> Unit,
    private val needsConfirmation: (T) -> Boolean,
    private val getDialogContent: (T) -> DialogContent,
    private val needsValidation: ((T) -> Boolean)? = null,
    private val getValidationDialogContent: ((T) -> DialogContent)? = null,
    private val onValidationConfirm: ((T) -> Unit)? = null,
) {
    var pendingIntent by mutableStateOf<T?>(null)
        private set
    
    var validationFailedIntent by mutableStateOf<T?>(null)
        private set
    
    fun handleIntent(intent: T) {
        if (needsValidation?.invoke(intent) == true) {
            validationFailedIntent = intent
        } else if (needsConfirmation(intent)) {
            pendingIntent = intent
        } else {
            onHandleIntent(intent)
        }
    }
    
    fun confirmPendingIntent() {
        pendingIntent?.let { intent ->
            onHandleIntent(intent)
            pendingIntent = null
        }
    }
    
    fun dismissPendingIntent() {
        pendingIntent = null
    }
    
    fun confirmValidationIntent() {
        validationFailedIntent?.let { intent ->
            onValidationConfirm?.invoke(intent)
            validationFailedIntent = null
        }
    }
    
    fun dismissValidationIntent() {
        validationFailedIntent = null
    }
    
    fun getDialogContentFor(intent: T): DialogContent = getDialogContent(intent)
    
    fun getValidationDialogContentFor(intent: T): DialogContent {
        return getValidationDialogContent?.invoke(intent) ?: DialogContent(
            title = Res.string.dialog_generic_validation_title,
            message = Res.string.dialog_generic_validation_message,
            confirmButtonText = Res.string.dialog_generic_ok_button
        )
    }
}

@Composable
fun <T> rememberConfirmationDialogHandler(
    onHandleIntent: (T) -> Unit,
    needsConfirmation: (T) -> Boolean,
    getDialogContent: (T) -> DialogContent,
    needsValidation: ((T) -> Boolean)? = null,
    getValidationDialogContent: ((T) -> DialogContent)? = null,
    onValidationConfirm: ((T) -> Unit)? = null,
): ConfirmationDialogState<T> {
    return remember(
        onHandleIntent,
        needsConfirmation,
        getDialogContent,
        needsValidation,
        getValidationDialogContent,
        onValidationConfirm
    ) {
        ConfirmationDialogState(
            onHandleIntent = onHandleIntent,
            needsConfirmation = needsConfirmation,
            getDialogContent = getDialogContent,
            needsValidation = needsValidation,
            getValidationDialogContent = getValidationDialogContent,
            onValidationConfirm = onValidationConfirm
        )
    }
}

@Composable
fun <T> ConfirmationDialogs(
    state: ConfirmationDialogState<T>
) {
    // Confirmation dialog for intents that need confirmation
    state.pendingIntent?.let { intent ->
        val dialogContent = state.getDialogContentFor(intent)
        ConfirmationDialog(
            title = stringResource(dialogContent.title),
            message = stringResource(dialogContent.message),
            confirmButtonText = stringResource(dialogContent.confirmButtonText),
            dismissButtonText = stringResource(dialogContent.dismissButtonText),
            onConfirm = { state.confirmPendingIntent() },
            onDismiss = { state.dismissPendingIntent() },
            isVisible = true,
        )
    }

    // Validation dialog for intents that failed validation
    state.validationFailedIntent?.let { intent ->
        val dialogContent = state.getValidationDialogContentFor(intent)
        ConfirmationDialog(
            title = stringResource(dialogContent.title),
            message = stringResource(dialogContent.message),
            confirmButtonText = stringResource(dialogContent.confirmButtonText),
            dismissButtonText = stringResource(dialogContent.dismissButtonText),
            onConfirm = { state.confirmValidationIntent() },
            onDismiss = { state.dismissValidationIntent() },
            isVisible = true,
        )
    }
}

data class DialogContent(
    val title: StringResource,
    val message: StringResource,
    val confirmButtonText: StringResource = Res.string.dialog_generic_confirm_button,
    val dismissButtonText: StringResource = Res.string.dialog_generic_dismiss_button,
)