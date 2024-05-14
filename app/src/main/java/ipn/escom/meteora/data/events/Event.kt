package ipn.escom.meteora.data.events

data class Event (
    val title: String,
    val description: String,
    val date: String, // dd-mm-yyyy format
    val time: String,
    val location: String
){
    fun getDay(): String {
        return date.split("-")[0]
    }

    fun getMonth(): String {
        return date.split("-")[1]
    }

    fun getYear(): String {
        return date.split("-")[2]
    }
}

