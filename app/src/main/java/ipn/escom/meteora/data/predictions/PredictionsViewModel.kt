package ipn.escom.meteora.data.predictions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipn.escom.meteora.data.predictions.data.network.response.PredictionsResponse
import ipn.escom.meteora.data.predictions.domain.PredictionsUseCase
import kotlinx.coroutines.launch

class PredictionsViewModel : ViewModel() {

    private val predictionsUseCase = PredictionsUseCase()

    private val _predictions = MutableLiveData<PredictionsResponse?>()
    val predictions: LiveData<PredictionsResponse?> = _predictions

    fun getPredictions(localityKey: String) {
        viewModelScope.launch {
            val response = predictionsUseCase(localityKey)
            _predictions.value = response
        }
    }
}