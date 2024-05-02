package ipn.escom.meteora.ui

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ipn.escom.meteora.data.weather.WeatherViewModel
import ipn.escom.meteora.data.weather.data.network.response.HourlyForecast
import ipn.escom.meteora.data.weather.getAnimatedIcon
import ipn.escom.meteora.data.weather.getDescription
import ipn.escom.meteora.utils.getDayOfWeekFromLong
import ipn.escom.meteora.utils.getLocalDateString
import ipn.escom.meteora.utils.getOnlyDateString

@Composable
fun WeatherDetailScreen(
    town: String?,
    timestamp: String?,
    weatherViewModel: WeatherViewModel,
    navController: NavController
) {

    val dailyForecast by weatherViewModel.dailyForecast.observeAsState()
    val hourlyForecast by weatherViewModel.hourlyForecast.observeAsState()
    val selectedDayForecast = dailyForecast?.list?.find { it.dt.toString() == timestamp }
    val selectedHourlyForecast = hourlyForecast?.list?.filter {
        getLocalDateString(it.dt) == getLocalDateString(
            selectedDayForecast?.dt ?: 0
        )
    }
    var showDialog by remember { mutableStateOf(false) }
    var selectedForecast by remember { mutableStateOf<HourlyForecast?>(null) }

    Scaffold(
        topBar = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = getOnlyDateString(selectedDayForecast?.dt ?: 0),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            item {
                selectedDayForecast?.let { weather ->
                    ParameterCard(
                        title = "",
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    ) {
                        CurrentWeatherContent(
                            location = town ?: "",
                            time = getDayOfWeekFromLong(weather.dt),
                            temperature = weather.temp.day,
                            feelsLike = weather.feels_like.day,
                            description = getDescription(weather.weather[0].icon),
                            animatedIcon = getAnimatedIcon(weather.weather[0].icon),
                        )
                    }

                    if(!selectedHourlyForecast.isNullOrEmpty()) {
                        Log.d("WeatherDetailScreen", "selectedHourlyForecast: $selectedHourlyForecast")
                        Text(
                            text = "Pronóstico por hora",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(20.dp)
                        )
                        LazyRow(modifier = Modifier.padding(horizontal = 16.dp)) {
                            items(selectedHourlyForecast.size) { index ->
                                HourlyWeatherCard(hourlyForecast = selectedHourlyForecast[index]) {
                                    selectedForecast = selectedHourlyForecast[index]
                                    showDialog = true
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                HourlyForecastDialog(
                                    showDialog = showDialog,
                                    onDismiss = { showDialog = false },
                                    selectedForecast = selectedHourlyForecast[index]
                                )
                            }
                        }
                    }

                    Text(
                        text = "Condiciones diarias",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(20.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(140.dp)
                    ) {
                        WindCardContent(
                            windSpeed = weather.speed,
                            windDirection = weather.deg,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        HumidityCard(
                            humity = weather.humidity,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }

                    SunriseSunsetCardContent(
                        sunriseHour = weather.sunrise,
                        sunsetHour = weather.sunset,
                        currentTime = weather.dt,
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}