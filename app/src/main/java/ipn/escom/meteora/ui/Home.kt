package ipn.escom.meteora.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import ipn.escom.meteora.R

@Composable
fun Home(navController: NavController?) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items =
        listOf(R.string.menu_home, R.string.menu_map, R.string.menu_agenda, R.string.menu_forecast)
    val icons = listOf(
        Icons.Rounded.Home,
        Icons.Rounded.Map,
        Icons.Rounded.CalendarMonth,
        Icons.Rounded.WbSunny
    )

    Column {
        Forecast(modifier = Modifier.weight(0.9f))
        NavigationBar(modifier = Modifier.weight(0.1f)) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selectedItem == index,
                    onClick = { selectedItem = index },
                    icon = {
                        Icon(
                            imageVector = icons[index],
                            contentDescription = stringResource(id = item)
                        )
                    },
                    label = { Text(stringResource(id = item)) })
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    Home(null)
}