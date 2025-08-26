package com.oiid.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.oiid.core.designsystem.generated.resources.GT_Walsheim_Pro_Black
import com.oiid.core.designsystem.generated.resources.GT_Walsheim_Pro_Bold
import com.oiid.core.designsystem.generated.resources.GT_Walsheim_Pro_Light
import com.oiid.core.designsystem.generated.resources.GT_Walsheim_Pro_Medium
import com.oiid.core.designsystem.generated.resources.GT_Walsheim_Pro_Regular
import com.oiid.core.designsystem.generated.resources.GT_Walsheim_Pro_Thin
import com.oiid.core.designsystem.generated.resources.GT_Walsheim_Pro_Ultra_Bold
import com.oiid.core.designsystem.generated.resources.GT_Walsheim_Pro_Ultra_Light
import com.oiid.core.designsystem.generated.resources.Res
import org.jetbrains.compose.resources.Font

val fontFamily: FontFamily
    @Composable get() = FontFamily(
        Font(Res.font.GT_Walsheim_Pro_Black, FontWeight.Black),
        Font(Res.font.GT_Walsheim_Pro_Bold, FontWeight.Bold),
        Font(Res.font.GT_Walsheim_Pro_Bold, FontWeight.SemiBold),
        Font(Res.font.GT_Walsheim_Pro_Medium, FontWeight.Medium),
        Font(Res.font.GT_Walsheim_Pro_Regular, FontWeight.Normal),
        Font(Res.font.GT_Walsheim_Pro_Light, FontWeight.Light),
        Font(Res.font.GT_Walsheim_Pro_Thin, FontWeight.Thin),
        Font(Res.font.GT_Walsheim_Pro_Ultra_Light, FontWeight.ExtraLight),
        Font(Res.font.GT_Walsheim_Pro_Ultra_Bold, FontWeight.ExtraBold),
    )
