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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ipn.escom.meteora.data.weather.WeatherViewModel
import ipn.escom.meteora.data.weather.getAnimatedIcon
import ipn.escom.meteora.data.weather.getDescription
import ipn.escom.meteora.utils.getDateString

@Composable
fun WeatherDetailScreen(
    town: String?,
    timestamp: String?,
    weatherViewModel: WeatherViewModel,
    navController: NavController
) {

    val dailyForecast by weatherViewModel.dailyForecast.observeAsState()
    val selectedDayForecast = dailyForecast?.list?.find { it.dt.toString() == timestamp }

    Scaffold(
        topBar = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = getDateString(selectedDayForecast?.dt ?: 0),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
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
                    ParameterCard(title = "", modifier = Modifier.padding(horizontal = 16.dp)) {
                        CurrentWeatherContent(
                            location = town ?: "",
                            time = getDateString(weather.dt),
                            temperature = weather.temp.day,
                            feelsLike = weather.feels_like.day,
                            description = getDescription(weather.weather[0].icon),
                            animatedIcon = getAnimatedIcon(weather.weather[0].icon),
                        )
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
                }
            }
        }
    }
}