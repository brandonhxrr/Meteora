package ipn.escom.meteora.ui.agenda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ipn.escom.meteora.R

@Composable
fun SortButton(modifier: Modifier, onSortChange: (Boolean) -> Unit) {
    var sortByRecent by remember { mutableStateOf(true) }
    IconButton(
        modifier = modifier.padding(horizontal = 16.dp),
        onClick = {
            sortByRecent = !sortByRecent
            onSortChange(sortByRecent)
        }) {
        Icon(
            painter = if (sortByRecent) painterResource(id = R.drawable.ascending) else painterResource(
                id = R.drawable.descending
            ),
            contentDescription = stringResource(R.string.sort),
            modifier = Modifier.size(42.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SortButtonPreview() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.next_events),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        SortButton(Modifier) { ascending ->

        }
    }
}
