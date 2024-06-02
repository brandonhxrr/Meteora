package ipn.escom.meteora.ui

import android.location.Location
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Tile
import com.google.android.gms.maps.model.TileProvider
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.TileOverlay
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberTileOverlayState
import ipn.escom.meteora.R
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

private const val temperaturePalette =
    "-65:821692;-55:821692;-45:821692;-40:821692;-30:8257DB;-20:208CEC;-10:20C4E8;0:23DDDD;10:C2FF28;20:FFF028;25:FFC228;30:FF5500;35:AA0000;40:880000;45:660000;50:440000"
private const val precipitationPalette =
    "0.000005:E0F7FA;0.000009:B2EBF2;0.000014:80DEEA;0.000023:4DD0E1;0.000046:26C6DA;0.000092:00BCD4;0.000231:7B1FA2;0.000463:6A1B9A;0.000694:4A148C;0.000926:311B92;0.001388:1A237E;0.002315:000000;0.023150:000000"
const val windPalette =
    "1:FFFFFF00;5:EECECC66;15:B364BCB3;25:3F213BCC;50:744CACE6;100:4600AFFF;200:0D1126FF"
const val cloudinessPalette =
    "0:FFFFFF00;10:FDFDFF19;20:FCFBFF26;30:FAFAFF33;40:F9F8FF4C;50:F7F7FF66;60:F6F5FF8C;70:F4F4FFBF;80:E9E9DFCC;90:DEDEDED8;100:D2D2D2FF;200:D2D2D2FF"
const val humidityPalette =
    "0:db1200;20:965700;40:ede100;60:8bd600;80:00a808;100:000099;100.1:000099"

@Composable
fun MapsScreen(modifier: Modifier, location: Location?, apiKey: String) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Temperatura", "Lluvia", "Viento", "Nubosidad", "Humedad")
    val icons = listOf(
        R.drawable.ic_temperature,
        R.drawable.ic_rain,
        R.drawable.ic_wind,
        R.drawable.ic_clouds,
        R.drawable.ic_humidity
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            options.forEachIndexed { index, label ->
                FilterChip(
                    selected = index == selectedIndex,
                    onClick = { selectedIndex = index },
                    label = { Text(text = label, style = MaterialTheme.typography.bodySmall) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = icons[index]),
                            contentDescription = label,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
        Box(
            modifier = Modifier.weight(1f)
        ) {
            WeatherMap(location = location, apiKey = apiKey, selectedMap = selectedIndex)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Legend(selectedMap = selectedIndex)
            }
        }
    }
}

@Composable
fun WeatherMap(location: Location?, apiKey: String, selectedMap: Int) {
    val latLong = LatLng(location?.latitude ?: 19.42847, location?.longitude ?: -99.12766)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLong, 10f)
    }
    val tileOverlayState = rememberTileOverlayState()

    var tileProvider by remember {
        mutableStateOf<TileProvider>(
            HttpTileProvider(
                256,
                256,
                "https://maps.openweathermap.org/maps/2.0/weather/TA2/{z}/{x}/{y}?appid=$apiKey"
            )
        )
    }

    LaunchedEffect(selectedMap) {
        tileProvider = when (selectedMap) {
            0 -> HttpTileProvider(
                256,
                256,
                "https://maps.openweathermap.org/maps/2.0/weather/TA2/{z}/{x}/{y}?appid=$apiKey&palette=$temperaturePalette&opacity=0.6"
            )

            1 -> HttpTileProvider(
                256,
                256,
                "https://maps.openweathermap.org/maps/2.0/weather/PR0/{z}/{x}/{y}?appid=$apiKey&palette=$precipitationPalette"
            )

            2 -> HttpTileProvider(
                256,
                256,
                "https://maps.openweathermap.org/maps/2.0/weather/WS10/{z}/{x}/{y}?appid=$apiKey&arrow_step=16&palette=$windPalette&opacity=1.0"
            )

            3 -> HttpTileProvider(
                256,
                256,
                "https://maps.openweathermap.org/maps/2.0/weather/CL/{z}/{x}/{y}?appid=$apiKey&palette=$cloudinessPalette&opacity=0.9"
            )

            4 -> HttpTileProvider(
                256,
                256,
                "https://maps.openweathermap.org/maps/2.0/weather/HRD0/{z}/{x}/{y}?appid=$apiKey&palette=$humidityPalette&opacity=0.5"
            )

            else -> HttpTileProvider(
                256,
                256,
                "https://maps.openweathermap.org/maps/2.0/weather/TA2/{z}/{x}/{y}?appid=$apiKey"
            )
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        TileOverlay(
            tileProvider = tileProvider,
            state = tileOverlayState
        )
    }
}


class HttpTileProvider(
    private val width: Int,
    private val height: Int,
    private val urlTemplate: String
) :
    TileProvider {

    override fun getTile(x: Int, y: Int, zoom: Int): Tile? {
        val url = urlTemplate.replace("{x}", x.toString()).replace("{y}", y.toString())
            .replace("{z}", zoom.toString())
        Log.d("MapsScreen", "Generated Tile URL: $url")

        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val tileData = inputStream.readBytes()
            inputStream.close()
            Tile(width, height, tileData)
        } catch (e: Exception) {
            Log.e("MapsScreen", "Error fetching tile: $e")
            null
        }
    }
}

@Composable
fun Legend(selectedMap: Int) {
    val (unit, legendData) = when (selectedMap) {
        0 -> "°C" to listOf(
            "-65" to Color(0xFF821692),
            "-55" to Color(0xFF821692),
            "-45" to Color(0xFF821692),
            "-40" to Color(0xFF821692),
            "-30" to Color(0xFF8257DB),
            "-20" to Color(0xFF208CEC),
            "-10" to Color(0xFF20C4E8),
            "0" to Color(0xFF23DDDD),
            "10" to Color(0xFFC2FF28),
            "20" to Color(0xFFFFF028),
            "25" to Color(0xFFFFC228),
            "30" to Color(0xFFFF5500),
            "35" to Color(0xFFAA0000),
            "40" to Color(0xFF880000),
            "45" to Color(0xFF660000),
            "50" to Color(0xFF440000)
        )

        1 -> "mm/s" to listOf(
            "0.000005" to Color(0xFFE0F7FA),
            "0.000009" to Color(0xFFB2EBF2),
            "0.000014" to Color(0xFF80DEEA),
            "0.000023" to Color(0xFF4DD0E1),
            "0.000046" to Color(0xFF26C6DA),
            "0.000092" to Color(0xFF00BCD4),
            "0.000231" to Color(0xFF7B1FA2),
            "0.000463" to Color(0xFF6A1B9A),
            "0.000694" to Color(0xFF4A148C),
            "0.000926" to Color(0xFF311B92),
            "0.001388" to Color(0xFF1A237E),
            "0.002315" to Color(0xFF000000),
            "0.023150" to Color(0xFF000000)
        )

        2 -> "m/s" to listOf(
            "1" to Color(0xFFFFFF00),
            "5" to Color(0xEECECC66),
            "15" to Color(0xB364BCB3),
            "25" to Color(0x3F213BCC),
            "50" to Color(0x744CACE6),
            "100" to Color(0x4600AFFF),
            "200" to Color(0x0D1126FF)
        )

        3 -> "%" to listOf(
            "0" to Color(0xFFFFFF00),
            "10" to Color(0xFDFDFF19),
            "20" to Color(0xFCFBFF26),
            "30" to Color(0xFAFAFF33),
            "40" to Color(0xF9F8FF4C),
            "50" to Color(0xF7F7FF66),
            "60" to Color(0xF6F5FF8C),
            "70" to Color(0xF4F4FFBF),
            "80" to Color(0xE9E9DFCC),
            "90" to Color(0xDEDEDED8),
            "100+" to Color(0xD2D2D2FF)
        )

        4 -> "%" to listOf(
            "0" to Color(0xFFDB1200),
            "20" to Color(0xFF965700),
            "40" to Color(0xFFEDE100),
            "60" to Color(0xFF8BD600),
            "80" to Color(0xFF00A808),
            "100+" to Color(0xFF000099)
        )

        else -> "" to emptyList()
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Color.White.copy(alpha = 0.8f))
            .padding(8.dp)
    ) {
        Text(
            text = unit,
            modifier = Modifier.padding(bottom = 8.dp).align(Alignment.End),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End
        )
        legendData.forEach { (value, color) ->
            Row(
                modifier = Modifier.padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(15.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Text(
                    text = value,
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

