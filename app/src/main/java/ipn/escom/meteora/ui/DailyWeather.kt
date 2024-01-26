package ipn.escom.meteora.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.R

@Composable
fun DailyWeather() {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(200.dp)
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
        ){
            Icon(
                imageVector = Icons.Rounded.WbSunny,
                contentDescription = "Sunny",
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.CenterHorizontally),
                tint = Color.Unspecified
            )
            Text(text = "LUN", modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 10.dp), style = MaterialTheme.typography.bodyMedium)
            Text(text = "24 °C", modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 4.dp), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DailyWeatherPreview() {
    DailyWeather()
}