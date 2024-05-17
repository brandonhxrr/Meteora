package ipn.escom.meteora.data.events.data.network.response

import ipn.escom.meteora.utils.getDayFromMillis
import ipn.escom.meteora.utils.getHourOfDayFromLong

data class EventResponse (
    val id: String? = "",
    val title: String,
    val description: String,
    val date: Long, // dd-mm-yyyy format
    val time: Long,
    val location: String
){
    fun getDay(): Int {
        return getDayFromMillis(date)
    }

    fun getMonth(): String {
        return ""// date.split("-")[1]
    }

    fun getYear(): String {
        return ""//date.split("-")[2]
    }
}

