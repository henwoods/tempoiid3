package oiid.core.base.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import oiid.core.base.platform.context.AppContext
import oiid.core.base.platform.intent.IntentManagerImpl
import oiid.core.base.platform.review.AppReviewManagerImpl
import oiid.core.base.platform.update.AppUpdateManagerImpl

@Composable
actual fun LocalManagerProvider(
    context: AppContext,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalAppReviewManager provides AppReviewManagerImpl(),
        LocalIntentManager provides IntentManagerImpl(),
        LocalAppUpdateManager provides AppUpdateManagerImpl(),
    ) {
        content()
    }
}
