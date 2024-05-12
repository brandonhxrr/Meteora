package ipn.escom.meteora.data.localities

import android.location.Location
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.NearMe
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import ipn.escom.meteora.utils.getLocalityFromPostalCode

@Composable
fun SearchBarWithDialog(
    localityViewModel: LocalityViewModel,
    postalCode: String,
    onLocationSelected: (Location) -> Unit
) {
    val searchText by localityViewModel.searchText.collectAsState(initial = "")
    val isSearching by localityViewModel.isSearching.collectAsState(initial = false)
    val localities by localityViewModel.localities.collectAsState(initial = availableLocalities)
    val currentLocality = getLocalityFromPostalCode(postalCode = postalCode)

    val suggestions = localities.filter { it.name.contains(searchText, ignoreCase = true) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var showSuggestions by remember { mutableStateOf(false) }

    TextField(
        value = searchText,
        onValueChange = { newQuery ->
            localityViewModel.onSearchTextChange(newQuery)
            localityViewModel.onSearchingChanged(true)
            showSuggestions = true
        },
        singleLine = true,
        shape = MaterialTheme.shapes.extraLarge,
        textStyle = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .padding(4.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        leadingIcon = {
            Icon(Icons.Rounded.NearMe, contentDescription = "Search")
        },
        trailingIcon = {
            if (searchText.isNotEmpty() && isSearching) {
                IconButton(modifier = Modifier.size(50.dp),
                    onClick = {
                        localityViewModel.onSearchTextChange("")
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }) {
                    Icon(Icons.Filled.Clear, contentDescription = "Clear")
                }
            }
        },
        placeholder = {
            Text(
                text = if (currentLocality != null && availableLocalities.any { it.name == currentLocality }) currentLocality else "Buscar localidad",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )

    DropdownMenu(
        expanded = showSuggestions,
        onDismissRequest = {
            showSuggestions = false
        },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(0.8f)
            .wrapContentSize(align = Alignment.TopStart, unbounded = false)
            .heightIn(max = 300.dp),
        properties = PopupProperties(
            focusable = false
        )
    ) {
        suggestions.forEach { locality ->
            DropdownMenuItem(onClick = {
                onLocationSelected(Location(locality.name).apply {
                    latitude = locality.latitude
                    longitude = locality.longitude
                })
                localityViewModel.onSearchTextChange(locality.name)
                localityViewModel.onSearchingChanged(false)
                focusManager.clearFocus()
                showSuggestions = false
            }, text = {
                Text(text = locality.name, style = MaterialTheme.typography.bodyMedium)
            })
        }
    }
}