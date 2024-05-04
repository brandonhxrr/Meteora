package ipn.escom.meteora.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import ipn.escom.meteora.data.weather.data.network.response.DailyForecast
import ipn.escom.meteora.data.weather.data.network.response.DailyForecastResponse
import ipn.escom.meteora.data.weather.getAnimatedIcon
import ipn.escom.meteora.utils.getDayOfWeekFromLong
import ipn.escom.meteora.utils.getOnlyDateString

@Composable
fun DailyWeather(
    town: String,
    dailyForecastResponse: DailyForecastResponse? = null,
    navController: NavController?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        dailyForecastResponse?.list?.drop(1)?.forEach {
            DailyWeatherCard(town, it, navController)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun DailyWeatherCard(town: String, dailyForecast: DailyForecast, navController: NavController?) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            navController?.navigate("dailyForecast/${town}/${dailyForecast.dt}")
        }) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column {
                Text(
                    text = getDayOfWeekFromLong(dailyForecast.dt),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = getOnlyDateString(dailyForecast.dt),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${dailyForecast.temp.max} °C / ${dailyForecast.temp.min} °C",
                modifier = Modifier.align(Alignment.CenterVertically),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(
                    getAnimatedIcon(dailyForecast.weather[0].icon)
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(50.dp)
            )


        }


    }
}

