package ipn.escom.meteora.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import ipn.escom.meteora.R
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getCurrentTime(): String {
    val currentTime = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    return currentTime.format(formatter)
}

fun convertMillisToTimeFormat(millis: Long): String {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.US)
    return dateFormat.format(Date(millis))
}

@Composable
fun getFormattedDate(time: Long): String {
    val zoneId = ZoneId.systemDefault()
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(time), zoneId)
    val formatter = DateTimeFormatter.ofPattern(stringResource(id = R.string.date_format))
    return dateTime.format(formatter)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}

fun getCurrentTimeLong(): Long {
    return LocalTime.now().toSecondOfDay().toLong()
}

fun getHourOfDayFromLong(time: Long): Int {
    val dateTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC)
    return dateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault()).hour
}

fun getDayFromLong(time: Long): Int {
    val dateTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC)
    return dateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault()).dayOfMonth
}

fun getLocalDateString(time: Long): String {
    val zoneId = ZoneId.systemDefault()
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(time), zoneId)
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return dateTime.format(formatter)
}

fun getDateString(time: Long): String {
    val dateTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC)
    val formatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy")
    return dateTime.format(formatter)
}

fun getDayOfWeekFromLong(time: Long): String {
    val zoneId = ZoneId.systemDefault()
    val dateTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC)
    return dateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(zoneId).dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}

fun getOnlyDateString(time: Long): String {
    val zoneId = ZoneId.systemDefault()
    val dateTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC)
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return dateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(zoneId).format(formatter)
}

fun getHourWithMinutesString(time: Long): String {
    val zoneId = ZoneId.systemDefault()
    val dateTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC)
    val hour = dateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(zoneId).hour % 12
    val hourFormatted = if (hour == 0) 12 else hour
    val minute =
        String.format("%02d", dateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(zoneId).minute)
    val amPm =
        if (dateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(zoneId).hour < 12) "am" else "pm"
    return "$hourFormatted:$minute $amPm"
}

fun getDayFromMillis(millis: Long): Int {
    val localDate = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
    return localDate.dayOfMonth
}

fun getHoursAndMinutesFromMillis(millis: Long): Pair<Int, Int> {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = millis
    }
    val hours = calendar.get(Calendar.HOUR_OF_DAY)
    val minutes = calendar.get(Calendar.MINUTE)
    return Pair(hours, minutes)
}

fun Long.getHoursAndMinutesDiff(other: Long): Pair<Int, Int> {
    val diffInSeconds = kotlin.math.abs(this - other)
    val hours = (diffInSeconds / 3600).toInt()
    val minutes = ((diffInSeconds % 3600) / 60).toInt()
    return Pair(hours, minutes)
}

fun formatSelectedDate(selectedDayMillis: Long): String {
    val localDate = Instant.ofEpochMilli(selectedDayMillis).atZone(ZoneId.of("UTC")).toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("EEE, d 'de' MMMM 'de' yyyy", Locale("es", "MX"))
    localDate.format(formatter)
    return localDate.format(formatter).replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale(
                "es",
                "MX"
            )
        ) else it.toString()
    }
}

@Composable
fun getMonthName(monthNumber: Int): String {

    val month: Int = when (monthNumber) {
        1 -> R.string.month_january
        2 -> R.string.month_february
        3 -> R.string.month_march
        4 -> R.string.month_april
        5 -> R.string.month_may
        6 -> R.string.month_june
        7 -> R.string.month_july
        8 -> R.string.month_august
        9 -> R.string.month_september
        10 -> R.string.month_october
        11 -> R.string.month_november
        12 -> R.string.month_december
        else -> R.string.month_january
    }

    return stringResource(id = month)
}