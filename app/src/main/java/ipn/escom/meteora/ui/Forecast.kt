package ipn.escom.meteora.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import ipn.escom.meteora.R
import ipn.escom.meteora.ui.theme.gray
import kotlinx.coroutines.tasks.await
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Locale

@Composable
fun Forecast(modifier: Modifier) {

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var location by remember { mutableStateOf<Location?>(null) }
    var municipality by remember { mutableStateOf<String?>(null) }
    var postalCode by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(key1 = true) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                val lastLocation = fusedLocationClient.lastLocation.await()
                location = lastLocation
                location?.let {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    if (addresses!!.isNotEmpty()) {
                        municipality = addresses[0].locality
                        postalCode = addresses[0].postalCode
                    }
                }
            } catch (e: Exception) {
                Log.e("LocationProvider", "Error getting location: ${e.message}", e)
            }
        } else {
            Log.e("LocationProvider", "Location permissions not granted")
        }
    }


    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Weather()
        Card(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = gray
            )
        ) {
            Row(
                modifier = Modifier.padding(8.dp)
            ) {

                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Location",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = postalCode?.let { getLocalityFromPostalCode(it) }
                        ?: "Ubicación no disponible",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = Color.White
                )
            }
        }

        Text(
            text = "Pronóstico",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(20.dp)
        )
        DailyWeather()
    }
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ForecastPreview() {
    Forecast(modifier = Modifier)
}