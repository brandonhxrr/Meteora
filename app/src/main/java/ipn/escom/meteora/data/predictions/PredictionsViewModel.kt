package ipn.escom.meteora.data.predictions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipn.escom.meteora.data.PreferencesViewModel
import ipn.escom.meteora.data.predictions.data.network.response.StringPredictionsResponse
import ipn.escom.meteora.data.predictions.data.network.response.toStringPredictionsResponse
import ipn.escom.meteora.data.predictions.domain.PredictionsUseCase
import kotlinx.coroutines.launch

class PredictionsViewModel(private val preferencesViewModel: PreferencesViewModel) : ViewModel() {

    private val predictionsUseCase = PredictionsUseCase()

    private val _predictions = MutableLiveData(StringPredictionsResponse())
    val predictions: LiveData<StringPredictionsResponse> = _predictions

    fun getPredictions(localityKey: String) {
        viewModelScope.launch {
            val response = predictionsUseCase(localityKey)
            val useMetric = preferencesViewModel.useMetric.value ?: true
            val showDecimals = preferencesViewModel.showDecimals.value ?: true
            _predictions.value = response.toStringPredictionsResponse(useMetric, showDecimals)
        }
    }
}
