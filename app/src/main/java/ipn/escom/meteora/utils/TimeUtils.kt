package ipn.escom.meteora.utils

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getCurrentTime(): String {
    val currentTime = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    return currentTime.format(formatter)
}

fun getCurrentTimeLong(): Long {
    return LocalTime.now().toSecondOfDay().toLong()
}

fun getHourOfDayFromLong(time: Long): Int {
    val dateTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC)
    return dateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault()).hour
}

fun getHourWithMinutesString(time: Long): String {
    val zoneId = ZoneId.systemDefault()
    val dateTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC)
    val hour = dateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(zoneId).hour % 12
    val hourFormatted = if (hour == 0) 12 else hour
    val minute = String.format("%02d", dateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(zoneId).minute)
    val amPm = if (dateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(zoneId).hour < 12) "am" else "pm"
    return "$hourFormatted:$minute $amPm"
}


fun Long.getHoursAndMinutesDiff(other: Long): Pair<Int, Int> {
    val diffInSeconds = kotlin.math.abs(this - other)
    val hours = (diffInSeconds / 3600).toInt()
    val minutes = ((diffInSeconds % 3600) / 60).toInt()
    return Pair(hours, minutes)
}