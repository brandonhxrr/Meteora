package ipn.escom.meteora.ui.agenda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.data.events.AgendaViewModel
import ipn.escom.meteora.data.events.data.network.response.EventResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(modifier: Modifier = Modifier) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val agendaViewModel = remember { AgendaViewModel() }
    val upcomingEvents by agendaViewModel.upcomingEvents.observeAsState()
    val pastEvents by agendaViewModel.pastEvents.observeAsState()
    var showDetailSheet by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<EventResponse?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
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
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Próximos eventos",
                        style = MaterialTheme.typography.titleMedium,
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

            if(pastEvents?.isNotEmpty() == true) {
                item {
                    Text(
                        "Eventos pasados",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                }

                items(pastEvents?.size ?: 0) { index ->
                    val event = pastEvents!![index]
                    EventItem(eventResponse = event, onClick = {
                        selectedEvent = event
                        showDetailSheet = true
                    })
                }
            }

            item {
                if (showBottomSheet) {
                    EventBottomSheet(
                        agendaViewModel = agendaViewModel,
                        sheetState = sheetState,
                        scope = coroutineScope,
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
    AgendaScreen()
}
