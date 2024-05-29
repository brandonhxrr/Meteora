package ipn.escom.meteora.data.localities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class LocalityViewModel: ViewModel() {

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _localities = MutableStateFlow(availableLocalities)
    val localities = searchText.combine(_localities) { text, localities ->

        if(text.isBlank()){
            availableLocalities
        }

        localities.filter { locality ->
            locality.name.contains(text, ignoreCase = true)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _localities.value
    )

    fun onSearchTextChanged(text: String) {
        _searchText.value = text
    }

    fun onSearchingChanged(isSearching: Boolean) {
        _isSearching.value = isSearching
    }
}