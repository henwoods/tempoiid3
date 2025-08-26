package cmp.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cmp.navigation.ComposeApp
import coil3.compose.LocalPlatformContext
import oiid.core.base.platform.LocalManagerProvider
import oiid.core.base.platform.context.LocalContext
import oiid.core.base.ui.LocalImageLoaderProvider
import oiid.core.base.ui.rememberImageLoader

@Composable
fun SharedApp(
    modifier: Modifier = Modifier,
) {
    LocalManagerProvider(LocalContext.current) {
        LocalImageLoaderProvider(rememberImageLoader(LocalPlatformContext.current)) {
            ComposeApp(modifier = modifier)
        }
    }
}
