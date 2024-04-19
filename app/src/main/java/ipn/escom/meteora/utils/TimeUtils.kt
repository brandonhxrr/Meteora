package ipn.escom.meteora.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun getCurrentTime(): String {
    val currentTime = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    return currentTime.format(formatter)
}