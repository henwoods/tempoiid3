package com.oiid.feature.feed

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.oiid.core.designsystem.generated.resources.Res
import com.oiid.core.designsystem.generated.resources.profile
import org.jetbrains.compose.resources.imageResource

@Composable
fun ProfileIconButton(onNavigateToProfile: () -> Unit) {
    IconButton(onClick = onNavigateToProfile) {
        Icon(
            bitmap = imageResource(Res.drawable.profile),
            contentDescription = "Profile",
        )
    }
}