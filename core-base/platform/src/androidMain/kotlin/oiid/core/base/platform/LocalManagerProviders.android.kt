package oiid.core.base.platform

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import oiid.core.base.platform.context.AppContext
import oiid.core.base.platform.context.activity
import oiid.core.base.platform.intent.IntentManagerImpl
import oiid.core.base.platform.review.AppReviewManagerImpl
import oiid.core.base.platform.update.AppUpdateManagerImpl

/**
 * Android-specific implementation of the LocalManagerProvider composable function.
 *
 * This implementation initializes the platform-specific manager instances required
 * by the application and provides them to the composition hierarchy through
 * CompositionLocal providers. The function handles the creation and provision of:
 *
 * - AppReviewManager: For managing in-app review requests through Google Play
 * - IntentManager: For handling Android-specific intent operations
 * - AppUpdateManager: For managing application updates through Google Play
 *
 * The function retrieves the current Activity from the provided AppContext and uses
 * it to initialize each manager implementation. All managers are then made available
 * to child composables through their respective CompositionLocal providers.
 *
 * @param context The Android Context used to initialize the managers
 * @param content The composable content where the managers will be available
 */
@Composable
actual fun LocalManagerProvider(
    context: AppContext,
    content: @Composable () -> Unit,
) {
    val activity = context.activity as Activity
    CompositionLocalProvider(
        LocalAppReviewManager provides AppReviewManagerImpl(activity),
        LocalIntentManager provides IntentManagerImpl(activity),
        LocalAppUpdateManager provides AppUpdateManagerImpl(activity),
    ) {
        content()
    }
}
