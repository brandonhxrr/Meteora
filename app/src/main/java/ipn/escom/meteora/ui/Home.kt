package ipn.escom.meteora.ui

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ipn.escom.meteora.R

@Composable
fun HomeScreen() {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(R.string.menu_home, R.string.menu_map, R.string.menu_calendar)
    val icons = listOf(R.drawable.home, R.drawable.map, R.drawable.calendar_month)

    Column {
        Forecast(modifier = Modifier.weight(0.9f))
        NavigationBar(modifier = Modifier.weight(0.1f)) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selectedItem == index,
                    onClick = { selectedItem = index },
                    icon = {
                        Icon(
                            painter = painterResource(id = icons[index]),
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
    HomeScreen()
}