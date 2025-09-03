package com.oiid.core.common

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

val DATE_TIME_FORMAT_ABBREVIATED = LocalDateTime.Format {
    monthName(MonthNames.ENGLISH_ABBREVIATED)
    char(' ')
    dayOfMonth()
    chars(", ")
    year()
}

fun formatRelativeTime(pastMillis: Instant, nowMillis: Long = Clock.System.now().toEpochMilliseconds()): String {
    val diff = nowMillis - pastMillis.toEpochMilliseconds()

    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val months = (days / 30)
    val years = (days / 365)

    return when {
        seconds < 60 -> "just now"
        minutes < 60 -> "$minutes minute${if (minutes != 1L) "s" else ""} ago"
        hours < 24 -> "$hours hour${if (hours != 1L) "s" else ""} ago"
        months < 1 -> { // If less than a month but more than 7 days, consider showing weeks
            val weeks = days / 7
            if (weeks > 0) "$weeks week${if (weeks != 1L) "s" else ""} ago"
            else "$days day${if (days != 1L) "s" else ""} ago" // Fallback if less than a week
        }
        months < 12 -> "$months month${if (months != 1L) "s" else ""} ago"
        else -> "$years year${if (years != 1L) "s" else ""} ago"
    }
}