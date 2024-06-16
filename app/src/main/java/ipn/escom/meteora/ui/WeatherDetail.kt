package ipn.escom.meteora.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import ipn.escom.meteora.R
import ipn.escom.meteora.data.PreferencesViewModel
import ipn.escom.meteora.data.weather.WeatherViewModel
import ipn.escom.meteora.data.weather.data.network.response.DailyForecast
import ipn.escom.meteora.data.weather.data.network.response.HourlyForecast
import ipn.escom.meteora.data.weather.getAnimatedIcon
import ipn.escom.meteora.data.weather.getDescription
import ipn.escom.meteora.ui.theme.getBackground
import ipn.escom.meteora.utils.getDayFromLong
import ipn.escom.meteora.utils.getDayOfWeekFromLong
import ipn.escom.meteora.utils.getFormattedDate
import ipn.escom.meteora.utils.getLocalDateString

@Composable
fun WeatherDetailScreen(
    preferencesViewModel: PreferencesViewModel,
    town: String?,
    timestamp: String?,
    weatherViewModel: WeatherViewModel,
    navController: NavController
) {

    val dailyForecast by weatherViewModel.dailyForecast.observeAsState()
    val hourlyForecast by weatherViewModel.hourlyForecast.observeAsState()
    var selectedDayForecast by remember {
        mutableStateOf(dailyForecast?.list?.find { it.dt.toString() == timestamp })
    }
    val showDecimals by preferencesViewModel.showDecimals.observeAsState(initial = false)
    var selectedHourlyForecast by remember {
        mutableStateOf(
            hourlyForecast?.list?.filter {
                getLocalDateString(it.dt) == getLocalDateString(
                    selectedDayForecast?.dt ?: 0
                )
            })
    }
    var showDialog by remember { mutableStateOf(false) }
    var selectedForecast by remember { mutableStateOf<HourlyForecast?>(null) }
    val dailyForecastList = dailyForecast!!.list.drop(1)

    var selectedIndex by remember {
        mutableIntStateOf(
            dailyForecastList.indexOfFirst { it.dt == selectedDayForecast?.dt }
        )
    }
    val scrollState = rememberLazyListState()

    Scaffold(
        containerColor = getBackground(),
        topBar = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.extended_forecast),
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
                LazyRow(modifier = Modifier.padding(16.dp), state = scrollState) {
                    items(dailyForecastList.size) { index ->
                        DailyForecastCard(
                            dailyForecast = dailyForecastList[index],
                            selected = dailyForecastList[index] == selectedDayForecast
                        ) {
                            selectedDayForecast = dailyForecastList[index]

                            selectedHourlyForecast =
                                hourlyForecast?.list?.filter { filteredForecast ->
                                    getLocalDateString(filteredForecast.dt) == getLocalDateString(
                                        selectedDayForecast?.dt ?: 0
                                    )
                                }
                            selectedIndex = index
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
                LaunchedEffect(key1 = selectedIndex) {
                    scrollState.animateScrollToItem(selectedIndex)
                }
            }
            item {
                selectedDayForecast?.let { weather ->
                    ParameterCard(
                        title = "",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        CurrentWeatherContent(
                            location = town ?: "",
                            time = getFormattedDate(weather.dt),
                            temperature = weather.temp.day,
                            feelsLike = weather.feels_like.day,
                            description = getDescription(weather.weather[0].icon),
                            animatedIcon = getAnimatedIcon(weather.weather[0].icon),
                            maxt = weather.temp.max,
                            mint = weather.temp.min,
                        )
                    }

                    if (!selectedHourlyForecast.isNullOrEmpty()) {
                        Text(
                            text = stringResource(id = R.string.hourly_forecast),
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(20.dp)
                        )
                        HourlyWeather(showDecimals, selectedHourlyForecast)
                    }

                    Text(
                        text = stringResource(id = R.string.daily_conditions),
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
                            windSpeed = weather.speed,
                            windDirection = weather.deg,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        HumidityCard(
                            humidity = weather.humidity,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(140.dp)
                    ) {
                        SunriseSunsetMiniCard(
                            sunriseHour = weather.sunrise,
                            sunsetHour = weather.sunset,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        RainIndicator(
                            rain = weather.rain,
                            pop = weather.pop,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun DailyForecastCard(dailyForecast: DailyForecast, selected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .height(120.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
        )
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(getAnimatedIcon(dailyForecast.weather[0].icon))
            )
            Text(
                text = "${getDayOfWeekFromLong(dailyForecast.dt)}, ${getDayFromLong(dailyForecast.dt)}",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 10.dp),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp
            )

            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.CenterHorizontally)
                    .size(36.dp),
            )

            Text(
                text = "${dailyForecast.temp.max.toInt()}° / ${dailyForecast.temp.min.toInt()}°",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 4.dp),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp
            )
            if (dailyForecast.pop > 0.0) {
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_rain),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${(dailyForecast.pop * 100).toInt()}%",
                        modifier = Modifier.align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}