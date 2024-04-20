package ipn.escom.meteora.ui

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Thermostat
import androidx.compose.material.icons.rounded.Water
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import ipn.escom.meteora.R
import ipn.escom.meteora.data.weather.WeatherCondition
import ipn.escom.meteora.data.weather.WeatherViewModel
import ipn.escom.meteora.utils.RequestLocationPermission
import ipn.escom.meteora.utils.getCurrentTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Forecast(modifier: Modifier, weatherViewModel: WeatherViewModel) {

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var location by remember { mutableStateOf<Location?>(null) }
    var postalCode by remember { mutableStateOf<String?>(null) }
    val temperature: Double by weatherViewModel.mainTemp.observeAsState(initial = 0.0)
    val description: String by weatherViewModel.weatherDescription.observeAsState(initial = "")
    val feelsLike: Double by weatherViewModel.mainFeelsLike.observeAsState(initial = 0.0)
    val humidity: Int by weatherViewModel.mainHumidity.observeAsState(initial = 0)
    val windSpeed: Double by weatherViewModel.windSpeed.observeAsState(initial = 0.0)
    val sunrise: Long by weatherViewModel.sysSunrise.observeAsState(initial = 0)
    val sunset: Long by weatherViewModel.sysSunset.observeAsState(initial = 0)
    val windDeg: Int by weatherViewModel.windDeg.observeAsState(initial = 0)
    val name: String by weatherViewModel.name.observeAsState(initial = "")
    val apiKey = stringResource(id = R.string.OpenWeatherAPIKEY)
    val isLocationPermissionGranted = remember { mutableStateOf(false) }
    val refreshState = rememberPullToRefreshState()
    val code: String by weatherViewModel.weatherIcon.observeAsState(initial = "")

    val weatherCondition = WeatherCondition(
        location = name,
        temperature = temperature,
        feelsLike = feelsLike,
        description = description,
        code = code
    )

    if (refreshState.isRefreshing) {
        LaunchedEffect(true) {
            weatherViewModel.getWeather(
                apiKey = apiKey, lat = location?.latitude!!, lon = location?.longitude!!
            )
            delay(1500)
            refreshState.endRefresh()
        }

    }

    RequestLocationPermission(isLocationPermissionGranted)

    LaunchedEffect(isLocationPermissionGranted.value, true) {
        location = getLocation(fusedLocationClient, isLocationPermissionGranted)
        postalCode = getPostalCode(context, location)
    }

    if (location != null) {
        weatherViewModel.getWeather(
            apiKey = apiKey, lat = location?.latitude!!, lon = location?.longitude!!
        )
        Log.d("Forecast", "Location: ${location?.latitude}, ${location?.longitude}")
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(refreshState.nestedScrollConnection)
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {

                Column {
                    LocationIndicator(postalCode)
                    CurrentWeatherContent(
                        location = weatherCondition.location,
                        time = getCurrentTime(),
                        temperature = weatherCondition.temperature,
                        feelsLike = weatherCondition.feelsLike,
                        description = weatherCondition.getDescription(),
                        icon = weatherCondition.getIconDrawable(),
                        animatedIcon = weatherCondition.getAnimatedIcon()
                    )

                    Text(
                        text = "Condiciones diarias",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(20.dp)
                    )
                    Row (modifier = Modifier.fillMaxWidth().height(160.dp)) {
                        WindCardContent(windSpeed = windSpeed, windDirection = windDeg, modifier = Modifier.weight(1f).fillMaxHeight())
                        HumidityCard(humity = humidity, modifier = Modifier.weight(1f).fillMaxHeight())
                    }

                    val weatherParameters = listOf(
                        WeatherObject(
                            value = "$temperature °C",
                            icon = Icons.Rounded.Thermostat,
                            contentDescription = "Temperatura",
                            iconColor = Color.Blue
                        ), WeatherObject(
                            value = "$feelsLike °C",
                            icon = Icons.Rounded.AcUnit,
                            contentDescription = "Sensación térmica",
                            iconColor = Color.Green
                        ), WeatherObject(
                            value = "$humidity %",
                            icon = Icons.Rounded.Water,
                            contentDescription = "Humedad",
                            iconColor = Color.Blue
                        ), WeatherObject(
                            value = "$windSpeed km/h",
                            icon = Icons.Rounded.Air,
                            contentDescription = "Velocidad del viento",
                            iconColor = Color.Cyan
                        )
                    )

                    WeatherParameters(weatherParameters)

                    Text(
                        text = "Pronóstico",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(20.dp)
                    )
                    DailyWeather()
                }
                PullToRefreshContainer(
                    state = refreshState, modifier = Modifier.align(Alignment.TopCenter)
                )
            }


        }
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

        Log.d("LocationIndicator", "Localuty: $selectedLocality")
        selectedLocality =
            if (selectedItem in localities) selectedItem!! else "Ubicación no disponible"

        Column(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { expanded = true }) {
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
                    DropdownMenuItem(onClick = {
                        selectedLocality = locality
                        expanded = false
                    }, text = {
                        Text(text = locality, style = MaterialTheme.typography.bodyMedium)
                    })
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
suspend fun getLocation(
    fusedLocationClient: FusedLocationProviderClient,
    isLocationPermissionGranted: MutableState<Boolean>
): Location? {
    if (isLocationPermissionGranted.value) {
        try {
            return fusedLocationClient.lastLocation.await()
        } catch (e: Exception) {
            Log.e("LocationProvider", "Error getting location: ${e.message}", e)
        }
    } else {
        Log.e("LocationProvider", "Location permissions not granted")
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
    return null
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