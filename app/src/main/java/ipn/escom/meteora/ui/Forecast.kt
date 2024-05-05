package ipn.escom.meteora.ui

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
import androidx.compose.material.icons.rounded.KeyboardArrowDown
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ipn.escom.meteora.R
import ipn.escom.meteora.data.weather.WeatherCondition
import ipn.escom.meteora.data.weather.WeatherViewModel
import ipn.escom.meteora.data.weather.data.network.response.WeatherResponse
import ipn.escom.meteora.utils.getCurrentTime
import ipn.escom.meteora.utils.getLocalityFromPostalCode
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Forecast(
    modifier: Modifier,
    weatherViewModel: WeatherViewModel,
    location: Location? = null,
    postalCode: String? = null,
    navController: NavController? = null
) {
    val apiKey = stringResource(id = R.string.OpenWeatherAPIKEY)
    val refreshState = rememberPullToRefreshState()
    val hourlyForecast by weatherViewModel.hourlyForecast.observeAsState(initial = null)
    val dailyForecast by weatherViewModel.dailyForecast.observeAsState(initial = null)
    val weather by weatherViewModel.weather.observeAsState(
        initial = WeatherResponse()
    )

    val weatherCondition = weather?.let {
        WeatherCondition(
            weather = it
        )
    }

    if (refreshState.isRefreshing) {

        LaunchedEffect(true) {
            weatherViewModel.getWeather(
                apiKey = apiKey, lat = location?.latitude!!, lon = location.longitude
            )

            weatherViewModel.getDailyForecast(
                apiKey = apiKey, lat = location.latitude, lon = location.longitude
            )

            weatherViewModel.getHourlyForecast(
                apiKey = apiKey, lat = location.latitude, lon = location.longitude
            )

            delay(1500)
            refreshState.endRefresh()
        }

    }

    if (location != null) {
        weatherViewModel.getWeather(
            apiKey = apiKey, lat = location.latitude, lon = location.longitude
        )
        weatherViewModel.getDailyForecast(
            apiKey = apiKey, lat = location.latitude, lon = location.longitude
        )

        weatherViewModel.getHourlyForecast(
            apiKey = apiKey, lat = location.latitude, lon = location.longitude
        )
        Log.d("Forecast", "Location: ${location.latitude}, ${location.longitude}")
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
                    ParameterCard(title = "", modifier = Modifier.padding(horizontal = 16.dp)) {
                        CurrentWeatherContent(
                            location = weather!!.name,
                            time = getCurrentTime(),
                            temperature = weather!!.main.temp,
                            feelsLike = weather!!.main.feelsLike,
                            description = weatherCondition!!.getDescription(),
                            icon = weatherCondition.getIconDrawable(),
                            animatedIcon = weatherCondition.getAnimatedIcon()
                        )
                    }

                    Text(
                        text = "Pronóstico por hora",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(20.dp)
                    )

                    HourlyWeather(hourlyForecast)

                    Text(
                        text = "Condiciones diarias",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(20.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(140.dp)
                    ) {
                        WindCardContent(
                            windSpeed = weather!!.wind.speed,
                            windDirection = weather!!.wind.deg,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        HumidityCard(
                            humity = weather!!.main.humidity,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    SunriseSunsetCardContent(
                        sunriseHour = weather!!.sys.sunrise,
                        sunsetHour = weather!!.sys.sunset,
                        currentTime = weather!!.dt,
                    )

                    Text(
                        text = "Pronóstico extendido",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(20.dp)
                    )
                    DailyWeather(weather!!.name, dailyForecast, navController)
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ForecastPreview() {
    Forecast(modifier = Modifier, WeatherViewModel())
}