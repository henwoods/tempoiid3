package cmp.navigation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import oiid.core.base.designsystem.theme.OiidTheme
import oiid.core.base.designsystem.theme.OiidTheme.typography

@Composable
fun ComingSoon() {
    Box(
        modifier = Modifier.fillMaxSize().background(OiidTheme.colorScheme.surface),
        contentAlignment = Alignment.Center,
    ) {
        Text("Coming soon", style = typography.titleLarge)
    }
}
