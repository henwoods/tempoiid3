package com.oiid.core.designsystem.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import oiid.core.base.designsystem.theme.Gradients

val oiidGradient = Brush.horizontalGradient(colors = listOf(primaryColor1, primaryColor2))
val oiidGradientReversed = Brush.horizontalGradient(colors = listOf(primaryColor2, primaryColor1))

val oiidGradient2 = Brush.horizontalGradient(colors = listOf(strokeColor1, strokeColor2))
val oiidGradient2Reversed = Brush.horizontalGradient(colors = listOf(strokeColor2, strokeColor1))

val oiidGradientGray = Brush.horizontalGradient(colors = listOf(oiidDarkGray, oiidGray))
val oiidGradientBlack = Brush.horizontalGradient(colors = listOf(Color.Black, Color.Black))
val oiidGradientWhite = Brush.horizontalGradient(colors = listOf(oiidLightGray, Color.White))

val oiidGradientGrayReversed = Brush.horizontalGradient(colors = listOf(oiidGray, oiidDarkGray))
val oiidGradientWhiteReversed = Brush.horizontalGradient(colors = listOf(Color.White, oiidLightGray))
val oiidGradientDisabled = Brush.horizontalGradient(colors = listOf(oiidGray, oiidLightGray))

object NocturneLightGradients : Gradients {
    override val pillBackground = oiidGradientGray
    override val pillBackgroundReversed = oiidGradient
    override val defaultGradient = oiidGradient
    override val playerGradient = oiidGradient
    override val feedLogoGradient = oiidGradientBlack
    override val priceLabelBackground = oiidGradient
    override val selectedTabItem = oiidGradientWhite
    override val unselectedTabItem = oiidGradientWhite
    override val buttonImageColor = oiidGradientGray
    override val buttonImageHighlightColor = oiidGradient
    override val profileBackground = oiidGradient
    override val bandAffiliateLabelBackground = oiidGradient
    override val postAuthorLabelBackground = oiidGradientWhite
}

object NocturneDarkGradients : Gradients {
    override val pillBackground = oiidGradientGray
    override val pillBackgroundReversed = oiidGradient
    override val defaultGradient = oiidGradient
    override val playerGradient = oiidGradient
    override val feedLogoGradient = oiidGradientWhite
    override val priceLabelBackground = oiidGradient
    override val selectedTabItem = oiidGradientWhite
    override val unselectedTabItem = oiidGradientWhite
    override val buttonImageColor = oiidGradientWhite
    override val buttonImageHighlightColor = oiidGradient
    override val profileBackground = oiidGradient
    override val bandAffiliateLabelBackground = oiidGradient
    override val postAuthorLabelBackground = oiidGradientWhite
}

object MezzoLightGradients : Gradients {
    override val pillBackground = oiidGradient
    override val pillBackgroundReversed = oiidGradientReversed
    override val defaultGradient = oiidGradient2
    override val playerGradient = oiidGradient
    override val feedLogoGradient = oiidGradientBlack
    override val priceLabelBackground = oiidGradient
    override val selectedTabItem = oiidGradientWhite
    override val unselectedTabItem = oiidGradientWhite
    override val buttonImageColor = oiidGradientGray
    override val buttonImageHighlightColor = oiidGradient
    override val profileBackground = oiidGradient2
    override val bandAffiliateLabelBackground = oiidGradient2
    override val postAuthorLabelBackground = oiidGradientWhite
}

object MezzoDarkGradients : Gradients {
    override val pillBackground = oiidGradient
    override val pillBackgroundReversed = oiidGradientReversed
    override val defaultGradient = oiidGradient
    override val playerGradient = oiidGradient
    override val feedLogoGradient = oiidGradientWhite
    override val priceLabelBackground = oiidGradient
    override val selectedTabItem = oiidGradientWhite
    override val unselectedTabItem = oiidGradientWhite
    override val buttonImageColor = oiidGradientWhite
    override val buttonImageHighlightColor = oiidGradient
    override val profileBackground = oiidGradient
    override val bandAffiliateLabelBackground = oiidGradient2
    override val postAuthorLabelBackground = oiidGradientWhite
}

object ForteLightGradients : Gradients {
    override val pillBackground = oiidGradient2
    override val pillBackgroundReversed = oiidGradient2Reversed
    override val defaultGradient = oiidGradient2
    override val playerGradient = oiidGradient
    override val feedLogoGradient = oiidGradient2
    override val priceLabelBackground = oiidGradient2
    override val selectedTabItem = oiidGradientWhite
    override val unselectedTabItem = oiidGradientWhite
    override val buttonImageColor = oiidGradientGray
    override val buttonImageHighlightColor = oiidGradient2
    override val profileBackground = oiidGradient2
    override val bandAffiliateLabelBackground = oiidGradient2
    override val postAuthorLabelBackground = oiidGradientWhite
}

object ForteDarkGradients : Gradients {
    override val pillBackground = oiidGradient2
    override val pillBackgroundReversed = oiidGradientReversed
    override val defaultGradient = oiidGradient
    override val playerGradient = oiidGradient
    override val feedLogoGradient = oiidGradient2
    override val priceLabelBackground = oiidGradient
    override val selectedTabItem = oiidGradientWhite
    override val unselectedTabItem = oiidGradientWhite
    override val buttonImageColor = oiidGradientWhite
    override val buttonImageHighlightColor = oiidGradient2
    override val profileBackground = oiidGradient
    override val bandAffiliateLabelBackground = oiidGradient2
    override val postAuthorLabelBackground = oiidGradientWhite
}