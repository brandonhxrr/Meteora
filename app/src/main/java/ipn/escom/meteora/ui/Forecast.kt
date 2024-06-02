package ipn.escom.meteora.ui

import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ipn.escom.meteora.R
import ipn.escom.meteora.data.PreferencesViewModel
import ipn.escom.meteora.data.weather.WeatherCondition
import ipn.escom.meteora.data.weather.WeatherViewModel
import ipn.escom.meteora.data.weather.data.network.response.DailyForecastResponse
import ipn.escom.meteora.data.weather.data.network.response.HourlyForecastResponse
import ipn.escom.meteora.data.weather.data.network.response.WeatherResponse
import ipn.escom.meteora.utils.getHourWithMinutesString
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Forecast(
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel,
    location: Location? = null,
    preferencesViewModel: PreferencesViewModel,
    navController: NavController? = null
) {
    val apiKey = stringResource(id = R.string.OpenWeatherAPIKEY)
    val refreshState = rememberPullToRefreshState()
    val hourlyForecast by weatherViewModel.hourlyForecast.observeAsState(initial = HourlyForecastResponse())
    val dailyForecast by weatherViewModel.dailyForecast.observeAsState(initial = DailyForecastResponse())
    val weather by weatherViewModel.weather.observeAsState(initial = WeatherResponse())
    val showDecimals by preferencesViewModel.showDecimals.observeAsState(initial = false)

    LaunchedEffect(location) {
        location?.let {
            weatherViewModel.getWeather(apiKey = apiKey, lat = it.latitude, lon = it.longitude)
            weatherViewModel.getDailyForecast(
                apiKey = apiKey,
                lat = it.latitude,
                lon = it.longitude
            )
            weatherViewModel.getHourlyForecast(
                apiKey = apiKey,
                lat = it.latitude,
                lon = it.longitude
            )
        }
    }

    if (refreshState.isRefreshing) {
        LaunchedEffect(true) {
            location?.let {
                weatherViewModel.getWeather(
                    apiKey = apiKey,
                    lat = it.latitude,
                    lon = it.longitude
                )
                weatherViewModel.getDailyForecast(
                    apiKey = apiKey,
                    lat = it.latitude,
                    lon = it.longitude
                )
                weatherViewModel.getHourlyForecast(
                    apiKey = apiKey,
                    lat = it.latitude,
                    lon = it.longitude
                )
            }
            delay(1500)
            refreshState.endRefresh()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(refreshState.nestedScrollConnection)
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    ParameterCard(title = "", modifier = Modifier.padding(horizontal = 16.dp)) {
                        CurrentWeatherContent(
                            showDecimals = showDecimals,
                            location = weather.name,
                            time = getHourWithMinutesString(weather.dt),//getCurrentTime(),
                            temperature = weather.main.temp,
                            feelsLike = weather.main.feelsLike,
                            description = WeatherCondition(weather).getDescription(),
                            animatedIcon = WeatherCondition(weather).getAnimatedIcon()
                        )
                    }

                    Text(
                        text = "Pronóstico por hora",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(20.dp)
                    )

                    HourlyWeather(showDecimals, hourlyForecast)

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
                            windSpeed = weather.wind.speed,
                            windDirection = weather.wind.deg,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        HumidityCard(
                            humity = weather.main.humidity,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    SunriseSunsetCardContent(
                        sunriseHour = weather.sys.sunrise,
                        sunsetHour = weather.sys.sunset,
                        currentTime = weather.dt,
                    )

                    Text(
                        text = "Pronóstico extendido",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(20.dp)
                    )
                    DailyWeather(showDecimals, weather.name, dailyForecast, navController)
                }
                PullToRefreshContainer(
                    state = refreshState, modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ForecastPreview() {
    val context = LocalContext.current
    Forecast(
        modifier = Modifier,
        preferencesViewModel = PreferencesViewModel(context),
        weatherViewModel = WeatherViewModel(context, PreferencesViewModel(context))
    )
}