package ipn.escom.meteora.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.data.predictions.PredictionsViewModel
import ipn.escom.meteora.data.predictions.data.network.response.PredictionsResponse
import ipn.escom.meteora.ui.theme.tabStyle
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun PredictionsScreen(
    modifier: Modifier,
    predictionsViewModel: PredictionsViewModel
) {

    val predictions by predictionsViewModel.predictions.observeAsState()
    var selectedTab by remember { mutableIntStateOf(1) }

    LaunchedEffect(key1 = true) {
        predictionsViewModel.getPredictions()
    }

    val today = LocalDate.now()
    val sixMonthsFromNow = today.plusMonths(6)

    val pastPredictions = predictions?.predictions?.map { localityPrediction ->
        val filteredYears = localityPrediction.years.map { yearPrediction ->
            val filteredMonths = yearPrediction.months.mapNotNull { monthPrediction ->
                val filteredDays = monthPrediction.days.filter { dayPrediction ->
                    val predictionDate =
                        LocalDate.of(yearPrediction.year, monthPrediction.month, dayPrediction.day)
                    predictionDate.isBefore(today)
                }
                if (filteredDays.isNotEmpty()) monthPrediction.copy(days = filteredDays) else null
            }
            yearPrediction.copy(months = filteredMonths)
        }
        localityPrediction.copy(years = filteredYears)
    }?.let { pastLocalityPredictions ->
        PredictionsResponse(predictions = pastLocalityPredictions)
    }

    val futurePredictions = predictions?.predictions?.map { localityPrediction ->
        val filteredYears = localityPrediction.years.map { yearPrediction ->
            val filteredMonths = yearPrediction.months.mapNotNull { monthPrediction ->
                val filteredDays = monthPrediction.days.filter { dayPrediction ->
                    val predictionDate =
                        LocalDate.of(yearPrediction.year, monthPrediction.month, dayPrediction.day)
                    predictionDate.isAfter(today) || predictionDate.isEqual(today)
                }
                if (filteredDays.isNotEmpty()) monthPrediction.copy(days = filteredDays) else null
            }
            yearPrediction.copy(months = filteredMonths)
        }
        localityPrediction.copy(years = filteredYears)
    }?.let { futureLocalityPredictions ->
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
                0 -> pastPredictions?.let { pastPredictionsResponse ->
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            PredictionsCard(predictionsResponse = pastPredictionsResponse)
                        }
                    }
                }

                1 -> futurePredictions?.let { futurePredictionsResponse ->
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            PredictionsCard(predictionsResponse = futurePredictionsResponse)
                        }
                    }
                }
            }
        }


    }
}