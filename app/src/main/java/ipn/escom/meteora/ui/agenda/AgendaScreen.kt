package ipn.escom.meteora.ui.agenda

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.data.PreferencesViewModel
import ipn.escom.meteora.data.events.AgendaViewModel
import ipn.escom.meteora.data.events.data.network.response.EventResponse
import ipn.escom.meteora.ui.theme.getBackground
import ipn.escom.meteora.ui.theme.getOnBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    modifier: Modifier = Modifier,
    preferencesViewModel: PreferencesViewModel,
    agendaViewModel: AgendaViewModel = AgendaViewModel()
) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val upcomingEvents by agendaViewModel.upcomingEvents.observeAsState()
    val pastEvents by agendaViewModel.pastEvents.observeAsState()
    var showDetailSheet by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<EventResponse?>(null) }
    var isExpanded: Boolean by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = getBackground(),
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = getOnBackground(),
                onClick = {
                showBottomSheet = true
            }) {
                Icon(Icons.Rounded.Add, contentDescription = "Add event")
            }
        },
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .animateContentSize()
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Próximos eventos",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterVertically)
                    )
                    SortButton(modifier = Modifier.align(Alignment.CenterVertically)) { ascending ->
                        agendaViewModel.onEventOrderChanged(ascending)
                    }
                }
            }

            items(upcomingEvents?.size ?: 0) { index ->
                val event = upcomingEvents!![index]
                EventItem(eventResponse = event, onClick = {
                    selectedEvent = event
                    showDetailSheet = true
                })
            }



            if (pastEvents?.isNotEmpty() == true) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isExpanded = !isExpanded }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Eventos pasados",
                            style = MaterialTheme.typography.titleSmall,
                            textAlign = TextAlign.Start
                        )
                        Icon(
                            imageVector = if (isExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                            contentDescription = if (isExpanded) "Cerrar" else "Abrir"
                        )
                    }
                }

                if(isExpanded){
                    items(pastEvents?.size ?: 0) { index ->
                        val event = pastEvents!![index]
                        EventItem(eventResponse = event, onClick = {
                            selectedEvent = event
                            showDetailSheet = true
                        })
                    }
                }
            }

            item {
                if (showBottomSheet) {
                    EventBottomSheet(
                        agendaViewModel = agendaViewModel,
                        sheetState = sheetState,
                        scope = coroutineScope,
                        preferencesViewModel = preferencesViewModel,
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                    )
                }
            }

            item {
                if (showDetailSheet && selectedEvent != null) {
                    EventBottomSheet(
                        agendaViewModel = agendaViewModel,
                        eventResponse = selectedEvent!!,
                        sheetState = sheetState,
                        scope = coroutineScope,
                        preferencesViewModel = preferencesViewModel,
                        onDismissRequest = {
                            showDetailSheet = false
                            selectedEvent = null
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AgendaScreenPreview() {
    AgendaScreen(preferencesViewModel = PreferencesViewModel(LocalContext.current))
}
