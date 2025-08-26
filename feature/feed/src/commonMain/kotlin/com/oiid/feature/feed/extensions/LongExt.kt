package com.oiid.feature.feed.extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

@OptIn(FormatStringsInDatetimeFormats::class)
val formatter = LocalDateTime.Format {
    byUnicodePattern("yyyy-MM-dd HH:mm:ss")
}

fun Long.formatDateTime(): String {
    val now: Instant = Instant.fromEpochMilliseconds(this)
    val dateTime: LocalDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
    return dateTime.format(formatter)
}
