package com.oiid.feature.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import com.oiid.core.oiidPainterResource
import com.oiid.core.stringResource
import oiid.core.base.designsystem.theme.OiidTheme

@Composable
fun OnboardingPage(modifier: Modifier = Modifier, page: OnboardingPage) {
    BoxWithConstraints(modifier.fillMaxSize()) {
        val screenHeight = maxHeight
        val imageHeight = screenHeight * 3f / 5f
        val textHeight = screenHeight - imageHeight

        Box {
            Image(
                painter = oiidPainterResource(page.image),
                contentDescription = null,
                modifier = Modifier.height(imageHeight).fillMaxWidth(),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopStart,
            )

            Box(
                modifier = Modifier.height(OiidTheme.spacing.xxl).fillMaxWidth().align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                OiidTheme.colorScheme.inverseSurface,
                            ),
                        ),
                    ),
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth().height(textHeight).align(Alignment.BottomStart).background(
                OiidTheme.colorScheme.inverseSurface,
            ).padding(OiidTheme.spacing.xl),
        ) {
            Text(
                color = OiidTheme.colorScheme.inverseOnSurface,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(page.title),
                style = OiidTheme.typography.titleLarge,
            )
            Text(
                color = OiidTheme.colorScheme.inverseOnSurface,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(page.description),
                style = OiidTheme.typography.bodyLarge,
            )
        }
    }
}
