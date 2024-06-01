package ipn.escom.meteora.ui

import android.location.Location
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.data.events.AgendaViewModel
import ipn.escom.meteora.data.events.data.network.response.EventResponse
import ipn.escom.meteora.data.localities.availableLocalities
import ipn.escom.meteora.data.predictions.PredictionsViewModel
import ipn.escom.meteora.data.predictions.data.network.response.PredictionsResponse
import ipn.escom.meteora.ui.agenda.EventBottomSheet
import ipn.escom.meteora.ui.login.AlertMessage
import ipn.escom.meteora.ui.theme.blue
import ipn.escom.meteora.ui.theme.indigo
import ipn.escom.meteora.ui.theme.orange
import ipn.escom.meteora.utils.getLocalityFromPostalCode
import ipn.escom.meteora.utils.getPostalCode
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredictionsScreen(
    modifier: Modifier = Modifier,
    location: Location? = null,
    predictionsViewModel: PredictionsViewModel,
    agendaViewModel: AgendaViewModel
) {
    val context = LocalContext.current
    val predictions by predictionsViewModel.predictions.observeAsState(initial = PredictionsResponse())
    var selectedTab by remember { mutableIntStateOf(1) }
    var postalCode: String? by remember { mutableStateOf("") }

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedEventResponse by remember { mutableStateOf<EventResponse?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var maxTemperatureEntries: List<Pair<Float, LocalDate>>
    var minTemperatureEntries: List<Pair<Float, LocalDate>>
    var rainEntries: List<Pair<Float, LocalDate>>

    LaunchedEffect(key1 = location) {
        location?.let {
            try {
                postalCode = getPostalCode(context, location)
                Log.d("PredictionsScreen", "Postal code: $postalCode")
                val localityName =
                    getLocalityFromPostalCode(context = context, postalCode = postalCode)
                loadPredictions(localityName, predictionsViewModel)
                maxTemperatureEntries = getMaxTemperatureEntries(predictions)
                minTemperatureEntries = getMinTemperatureEntries(predictions)
                rainEntries = getRainEntries(predictions)
            } catch (e: Exception) {
                Log.e("PredictionsScreen", "Error fetching postal code or locality: ${e.message}")
            }
        }
    }

    if (predictions == PredictionsResponse()) {
        PredictionsEmpty {
            coroutineScope.launch {
                location?.let {
                    try {
                        Log.d(
                            "PredictionsScreen",
                            "Locality: ${location.latitude}, ${location.longitude}"
                        )
                        postalCode = getPostalCode(context, location)
                        Log.d("PredictionsScreen", "Postal code: $postalCode")
                        val localityName =
                            getLocalityFromPostalCode(context, postalCode)
                        loadPredictions(localityName, predictionsViewModel)
                    } catch (e: Exception) {
                        Log.e(
                            "PredictionsScreen",
                            "Error fetching postal code or locality: ${e.message}"
                        )
                    }
                }
            }
        }
    } else {

        val today = LocalDate.now()

        val pastPredictions = predictions.predictions.map { localityPrediction ->
            val filteredYears = localityPrediction.years.map { yearPrediction ->
                val filteredMonths = yearPrediction.months.mapNotNull { monthPrediction ->
                    val filteredDays = monthPrediction.days.filter { dayPrediction ->
                        val predictionDate =
                            LocalDate.of(
                                yearPrediction.year,
                                monthPrediction.month,
                                dayPrediction.day
                            )
                        predictionDate.isBefore(today)
                    }
                    if (filteredDays.isNotEmpty()) monthPrediction.copy(days = filteredDays) else null
                }
                yearPrediction.copy(months = filteredMonths)
            }
            localityPrediction.copy(years = filteredYears)
        }.let { pastLocalityPredictions ->
            PredictionsResponse(predictions = pastLocalityPredictions)
        }

        val futurePredictions = predictions.predictions.map { localityPrediction ->
            val filteredYears = localityPrediction.years.map { yearPrediction ->
                val filteredMonths = yearPrediction.months.mapNotNull { monthPrediction ->
                    val filteredDays = monthPrediction.days.filter { dayPrediction ->
                        val predictionDate =
                            LocalDate.of(
                                yearPrediction.year,
                                monthPrediction.month,
                                dayPrediction.day
                            )
                        predictionDate.isAfter(today) || predictionDate.isEqual(today)
                    }
                    if (filteredDays.isNotEmpty()) monthPrediction.copy(days = filteredDays) else null
                }
                yearPrediction.copy(months = filteredMonths)
            }
            localityPrediction.copy(years = filteredYears)
        }.let { futureLocalityPredictions ->
            PredictionsResponse(predictions = futureLocalityPredictions)
        }

        Column(modifier = modifier.fillMaxSize()) {
            TabRow(selectedTabIndex = selectedTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp),
                divider = { HorizontalDivider(color = MaterialTheme.colorScheme.background) }) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                ) {
                    Text(
                        text = "Pasadas",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                ) {
                    Text(
                        text = "Futuras",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                ) {
                    Text(
                        text = "Gráficas",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = FastOutSlowInEasing
                        )
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = FastOutSlowInEasing
                        )
                    )
                }, label = ""
            ) { tab ->
                when (tab) {
                    0 ->
                        if (pastPredictions.predictions.isNotEmpty()) {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                item {
                                    AlertMessage("Las predicciones son generadas con modelos de Aprendizaje automático (IA), por lo que pueden ser no totalmente precisas.")
                                    PredictionsCard(
                                        predictionsResponse = pastPredictions,
                                        onDayClick = { eventResponse ->
                                            selectedEventResponse = eventResponse
                                            showBottomSheet = true
                                        }
                                    )
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "No hay predicciones pasadas",
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }

                    1 ->
                        if (futurePredictions.predictions.isNotEmpty()) {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                item {
                                    AlertMessage("Las predicciones son generadas con modelos de Aprendizaje automático (IA), por lo que pueden ser no totalmente precisas.")
                                    PredictionsCard(
                                        predictionsResponse = futurePredictions,
                                        onDayClick = { eventResponse ->
                                            selectedEventResponse = eventResponse
                                            showBottomSheet = true
                                        }
                                    )
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "No hay predicciones futuras",
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                    2 -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            item {
                                AlertMessage("Las predicciones son generadas con modelos de Aprendizaje automático (IA), por lo que pueden ser no totalmente precisas.")

                                Text(
                                    "Temperatura máxima",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                )
                                maxTemperatureEntries = getMaxTemperatureEntries(predictions)
                                SimpleLineChart(maxTemperatureEntries, orange)
                            }

                            item {
                                Text(
                                    "Temperatura mínima",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                )
                                minTemperatureEntries = getMinTemperatureEntries(predictions)
                                SimpleLineChart(minTemperatureEntries, indigo)
                            }

                            item {
                                Text(
                                    "Nivel de lluvia",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                )
                                rainEntries = getRainEntries(predictions)
                                SimpleLineChart(rainEntries, blue)
                            }
                        }
                    }
                }
            }
        }

        if (showBottomSheet && selectedEventResponse != null) {
            EventBottomSheet(
                agendaViewModel = agendaViewModel,
                eventResponse = selectedEventResponse,
                sheetState = sheetState,
                scope = coroutineScope,
                onDismissRequest = {
                    showBottomSheet = false
                }
            )
        }
    }
}

suspend fun loadPredictions(
    localityName: String?,
    predictionsViewModel: PredictionsViewModel
) {
    Log.d("PredictionsScreen", "Locality name: $localityName")
    val locality = availableLocalities.find { it.name == localityName }
    if (locality != null) {
        try {
            predictionsViewModel.getPredictions(locality.key)
        } catch (e: Exception) {
            Log.e("PredictionsScreen", "Error loading predictions: ${e.message}")
        }
    }
}
