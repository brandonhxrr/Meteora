package ipn.escom.meteora.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ipn.escom.meteora.data.predictions.PredictionsViewModel

@Composable
fun PredictionsScreen(
    modifier: Modifier,
    predictionsViewModel: PredictionsViewModel,
    navController: NavController? = null
) {

    val predictions by predictionsViewModel.predictions.observeAsState()

    LaunchedEffect(key1 = true) {
        predictionsViewModel.getPredictions()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Text(text = "Predicciones", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            predictions?.let { PredictionsCard(predictionsResponse = it) }
        }

    }

}