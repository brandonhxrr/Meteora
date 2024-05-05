package ipn.escom.meteora.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.FusedLocationProviderClient
import ipn.escom.meteora.R
import ipn.escom.meteora.ui.isInternetAvailable
import ipn.escom.meteora.ui.isNetworkAvailable
import kotlinx.coroutines.tasks.await
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Locale

@SuppressLint("MissingPermission")
suspend fun getLocation(
    fusedLocationClient: FusedLocationProviderClient,
    isLocationPermissionGranted: MutableState<Boolean>,
    context: Context
): Location? {
    if (isLocationPermissionGranted.value && isNetworkAvailable(context) && isInternetAvailable()) {
        try {
            return fusedLocationClient.lastLocation.await()
        } catch (e: Exception) {
            Log.e("LocationProvider", "Error getting location: ${e.message}", e)
        }
    } else {
        Log.e("LocationProvider", "Location permissions not granted or no internet connection")
    }
    return null
}

fun getPostalCode(
    context: Context, location: Location?
): String? {
    location?.let {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                return addresses[0].postalCode
            }
        }
    }
    return ""
}

@Composable
fun getLocalityFromPostalCode(postalCode: String?): String? {
    val context = LocalContext.current
    val inputStream = context.resources.openRawResource(R.raw.postal_codes)
    val reader = BufferedReader(InputStreamReader(inputStream))
    var line: String?
    while (reader.readLine().also { line = it } != null) {
        val parts = line!!.split(",")
        if (parts.size == 2 && parts[0] == postalCode) {
            return parts[1]
        }
    }
    return null
}