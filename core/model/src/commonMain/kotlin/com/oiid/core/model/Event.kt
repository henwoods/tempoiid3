package com.oiid.core.model

import co.touchlab.kermit.Logger
import com.oiid.core.common.DATE_TIME_FORMAT_ABBREVIATED
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    @SerialName("eventId")
    val eventId: String,
    @SerialName("artistId")
    val artistId: String,
    @SerialName("eventTime")
    val eventTime: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("link")
    val link: String? = null,
    @SerialName("venueName")
    val venueName: String? = null,
    @SerialName("venueAddress")
    val venueAddress: String? = null,
    @SerialName("endTime")
    val endTime: String? = null,
)

@Serializable
data class EventDetail(
    @SerialName("eventId")
    val eventId: String,
    @SerialName("artistId")
    val artistId: String,
    @SerialName("eventTime")
    val eventTime: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("link")
    val link: String? = null,
    @SerialName("venueName")
    val venueName: String? = null,
    @SerialName("venueAddress")
    val venueAddress: String? = null,
    @SerialName("endTime")
    val endTime: String? = null,
)

fun Event.isInThePast(): Boolean {
    return try {
        val eventInstant = Instant.parse(eventTime)
        val now = kotlinx.datetime.Clock.System.now()
        now > eventInstant
    } catch (e: Exception) {
        false
    }
}

fun Event.getEventInstant(): Instant? {
    return try {
        Instant.parse(eventTime)
    } catch (e: Exception) {
        Logger.e(e) { "Failed to parse event time: $eventTime" }
        null
    }
}

fun Event.getEndInstant(): Instant? {
    return try {
        endTime?.let { Instant.parse(it) }
    } catch (e: Exception) {
        Logger.e(e) { "Failed to parse event time: $eventTime" }
        null
    }
}

fun Event.formattedEventTime(): String {
    return try {
        val instant = Instant.parse(eventTime)
        val timeZone: TimeZone = TimeZone.currentSystemDefault()
        val dateTime: LocalDateTime = instant.toLocalDateTime(timeZone)
        dateTime.format(DATE_TIME_FORMAT_ABBREVIATED)
    } catch (_: Exception) {
        eventTime
    }
}

fun EventDetail.toEvent(): Event = Event(
    eventId = eventId,
    artistId = artistId,
    eventTime = eventTime,
    title = title,
    description = description,
    link = link,
    venueName = venueName,
    venueAddress = venueAddress,
    endTime = endTime,
)
