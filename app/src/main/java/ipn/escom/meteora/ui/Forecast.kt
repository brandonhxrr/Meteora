package ipn.escom.meteora.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.ui.theme.gray

@Composable
fun Forecast(modifier: Modifier) {

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Weather()
        Card(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = gray
            )
        ) {
            Row (
                modifier = Modifier.padding(8.dp)
            ){

                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Location",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Gustavo A. Madero",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = Color.White
                )
            }
        }

        Text(
            text = "Pronóstico",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(20.dp)
        )
        DailyWeather()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ForecastPreview() {
    Forecast(modifier = Modifier)
}