package ipn.escom.meteora.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import ipn.escom.meteora.R
import ipn.escom.meteora.data.weather.WeatherCondition
import ipn.escom.meteora.data.weather.data.network.response.DailyForecastResponse
import ipn.escom.meteora.data.weather.data.network.response.HourlyForecast
import ipn.escom.meteora.data.weather.data.network.response.HourlyForecastResponse
import ipn.escom.meteora.data.weather.getAnimatedIcon
import ipn.escom.meteora.ui.theme.amber
import ipn.escom.meteora.utils.getHourWithMinutesString

@Composable
fun DailyWeather(hourlyForecastResponse: HourlyForecastResponse? = null) {
    LazyRow(modifier = Modifier.padding(horizontal = 16.dp)){
        if(hourlyForecastResponse != null){
            items(24) { index ->
                HourlyWeather(hourlyForecastResponse.list[index])
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }

}

@Composable
fun HourlyWeather(hourlyForecast: HourlyForecast) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(180.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ){
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(getAnimatedIcon(hourlyForecast.weather[0].icon))
            )
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.CenterHorizontally)
                    .size(48.dp),
            )
            Text(text = getHourWithMinutesString(hourlyForecast.dt), modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 10.dp), style = MaterialTheme.typography.bodyLarge)
            Text(text = hourlyForecast.main.temp.toString() + " °C", modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 4.dp), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DailyWeatherPreview() {
    DailyWeather()
}