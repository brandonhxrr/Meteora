package ipn.escom.meteora.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import ipn.escom.meteora.R
import ipn.escom.meteora.data.weather.WeatherViewModel
import kotlinx.coroutines.tasks.await
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Locale

@Composable
fun Forecast(modifier: Modifier, weatherViewModel: WeatherViewModel) {

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var location by remember { mutableStateOf<Location?>(null) }
    var municipality by remember { mutableStateOf<String?>(null) }
    var postalCode by remember { mutableStateOf<String?>(null) }
    val temperature: Double by weatherViewModel.mainTemp.observeAsState(initial = 0.0)
    val description: String by weatherViewModel.weatherDescription.observeAsState(initial = "")
    val feelsLike: Double by weatherViewModel.mainFeelsLike.observeAsState(initial = 0.0)
    val humidity: Int by weatherViewModel.mainHumidity.observeAsState(initial = 0)
    val windSpeed: Double by weatherViewModel.windSpeed.observeAsState(initial = 0.0)
    val name: String by weatherViewModel.name.observeAsState(initial = "")
    val apiKey = stringResource(id = R.string.OpenWeatherAPIKEY)

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
                    weatherViewModel.getWeather(apiKey, it.latitude, it.longitude)
                    Log.d("Forecast", "Location: ${it.latitude}, ${it.longitude}")
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
        LocationIndicator(postalCode)
        Weather(temperature, description, feelsLike, humidity, windSpeed, name)

        WeatherParameters(feelsLike, humidity, windSpeed)

        Text(
            text = "Pronóstico",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(20.dp)
        )
        DailyWeather()
    }
}

@Composable
fun LocationIndicator(postalCode: String? = null) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        var expanded by remember { mutableStateOf(false) }
        val localities = listOf(
            "Álvaro Obregón",
            "Azcapotzalco",
            "Benito Juárez",
            "Coyoacán",
            "Cuajimalpa de Morelos",
            "Cuauhtémoc",
            "Gustavo A. Madero",
            "Iztacalco",
            "Iztapalapa",
            "Magdalena Contreras",
            "Miguel Hidalgo",
            "Milpa Alta",
            "Tláhuac",
            "Tlalpan",
            "Venustiano Carranza",
            "Xochimilco"
        )
        val selectedItem = postalCode?.let { getLocalityFromPostalCode(it) }

        var selectedLocality by remember { mutableStateOf("") }
        selectedLocality =
            if (selectedItem in localities) selectedItem!! else "Ubicación no disponible"

        Column(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { expanded = true }
            ) {
                Text(
                    text = selectedLocality,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "Expand",
                    modifier = Modifier.size(20.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .padding(8.dp)
                    .height(300.dp),
            ) {
                localities.forEach { locality ->
                    DropdownMenuItem(
                        onClick = {
                            selectedLocality = locality
                            expanded = false
                        },
                        text = {
                            Text(text = locality, style = MaterialTheme.typography.bodyMedium)
                        }
                    )
                }
            }
        }
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
    Forecast(modifier = Modifier, WeatherViewModel())
}