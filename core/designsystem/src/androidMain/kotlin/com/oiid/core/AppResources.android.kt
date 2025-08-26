package com.oiid.core

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource as androidPainterResource
import androidx.compose.ui.res.stringResource as androidStringResource

@SuppressLint("DiscouragedApi")
@Composable
actual fun oiidPainterResource(name: String): Painter {
    val context = LocalContext.current
    val resourceId = context.resources.getIdentifier(name, "drawable", context.packageName)
    if (resourceId == 0) {
        throw IllegalArgumentException("No drawable resource found with name: $name in package ${context.packageName}")
    }
    return androidPainterResource(id = resourceId)
}

@SuppressLint("DiscouragedApi")
@Composable
actual fun stringResource(name: String): String {
    val context = LocalContext.current
    val resourceId = context.resources.getIdentifier(name, "string", context.packageName)
    if (resourceId == 0) {
        throw IllegalArgumentException("No string resource found with name: $name in package ${context.packageName}")
    }
    return androidStringResource(id = resourceId)
}

@Composable
actual fun getPackageName(): String {
    return LocalContext.current.packageName
}
