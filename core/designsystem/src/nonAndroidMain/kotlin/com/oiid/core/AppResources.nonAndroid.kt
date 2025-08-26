package com.oiid.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.oiid.core.designsystem.generated.resources.Res
import com.oiid.core.designsystem.generated.resources.artist_header_light
import com.oiid.core.designsystem.generated.resources.auth_header
import com.oiid.core.designsystem.generated.resources.home_tab_icon
import com.oiid.core.designsystem.generated.resources.welcome_1
import com.oiid.core.designsystem.generated.resources.welcome_2
import com.oiid.core.designsystem.generated.resources.welcome_3
import com.oiid.core.designsystem.generated.resources.welcome_4
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun oiidPainterResource(name: String): Painter {
    return when (name) {
        "artist_header" -> {
            painterResource(Res.drawable.artist_header_light)
        }

        "auth_header" -> {
            painterResource(Res.drawable.auth_header)
        }

        "home_tab_icon" -> {
            painterResource(Res.drawable.home_tab_icon)
        }

        "welcome_1" -> {
            painterResource(Res.drawable.welcome_1)
        }

        "welcome_2" -> {
            painterResource(Res.drawable.welcome_2)
        }

        "welcome_3" -> {
            painterResource(Res.drawable.welcome_3)
        }

        "welcome_4" -> {
            painterResource(Res.drawable.welcome_4)
        }

        else -> {
            return painterResource(Res.drawable.home_tab_icon)
        }
    }
}

@Composable
actual fun stringResource(name: String): String {
    return ""
}

@Composable
actual fun getPackageName(): String {
    return ""
}
