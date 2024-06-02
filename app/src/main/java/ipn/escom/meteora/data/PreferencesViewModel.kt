package ipn.escom.meteora.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipn.escom.meteora.PreferencesManager
import kotlinx.coroutines.launch

class PreferencesViewModel(context: Context) : ViewModel() {

    private val preferencesManager = PreferencesManager(context)

    private val _useMetric = MutableLiveData<Boolean>()
    val useMetric: LiveData<Boolean> get() = _useMetric

    private val _showDecimals = MutableLiveData<Boolean>()
    val showDecimals: LiveData<Boolean> get() = _showDecimals

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            _useMetric.value = preferencesManager.getBooleanData("use_metric", true)
            _showDecimals.value = preferencesManager.getBooleanData("show_decimals", false)
        }
    }

    fun setUseMetric(useMetric: Boolean) {
        _useMetric.value = useMetric
        preferencesManager.saveBooleanData("use_metric", useMetric)
    }

    fun setShowDecimals(showDecimals: Boolean) {
        _showDecimals.value = showDecimals
        preferencesManager.saveBooleanData("show_decimals", showDecimals)
    }
}
