package com.oiid.feature.feed.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.oiid.core.designsystem.diagonalCornerShape

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    shape: Shape = diagonalCornerShape(),
    onCardClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Card(
        shape = shape,
        modifier = modifier.fillMaxWidth().padding(horizontal = 0.dp).shadow(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onCardClick,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}
