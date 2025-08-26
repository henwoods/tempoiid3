package oiid.core.base.platform.context

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import oiid.core.base.platform.context.AppContext.Companion.INSTANCE
import org.koin.dsl.module

val AppContextModule = module { single<AppContext> { INSTANCE } }

actual abstract class AppContext private constructor() {
    companion object {
        val INSTANCE = object : AppContext() {}
    }
}

actual val LocalContext: ProvidableCompositionLocal<AppContext>
    get() = staticCompositionLocalOf { AppContext.INSTANCE }

actual val AppContext.activity: Any
    @Composable
    get() = AppContext.INSTANCE
