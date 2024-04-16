package ipn.escom.meteora.ui

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Maps(modifier: Modifier) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Temperatura", "Lluvia", "Viento")

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            when (selectedIndex) {
                0 -> MeteoblueWidget("temperature")
                1 -> MeteoblueWidget("cloudsAndPrecipitation")
                2 -> MeteoblueWidget("windAnimation")
            }
        }

        SingleChoiceSegmentedButtonRow(modifier = Modifier.padding(16.dp)) {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    selected = index == selectedIndex,
                    onClick = { selectedIndex = index },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size)
                ) {
                    Text(text = label)
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MeteoblueWidget(parameter: String) {

    val iframe = """
            <iframe src="https://www.meteoblue.com/en/weather/maps/widget?windAnimation=0&gust=0&satellite=0&cloudsAndPrecipitation=0&temperature=0&sunshine=0&extremeForecastIndex=0&${parameter}=1&geoloc=detect&tempunit=C&windunit=km%252Fh&lengthunit=metric&zoom=10&autowidth=auto"  frameborder="0" scrolling="NO" allowtransparency="true" sandbox="allow-same-origin allow-scripts allow-popups allow-popups-to-escape-sandbox" style="width: 100%; height: 5 20px"></iframe>
        """.trimIndent()

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadData(iframe, "text/html", "utf-8")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MapsPreview() {
    Maps(modifier = Modifier)
}