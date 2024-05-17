package ipn.escom.meteora.data.events

import ipn.escom.meteora.utils.getHourOfDayFromLong

data class Event (
    val title: String,
    val description: String,
    val date: Long, // dd-mm-yyyy format
    val time: Long,
    val location: String
){
    fun getDay(): Int {
        return getHourOfDayFromLong(date)
    }

    fun getMonth(): String {
        return ""// date.split("-")[1]
    }

    fun getYear(): String {
        return ""//date.split("-")[2]
    }
}

