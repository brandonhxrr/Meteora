package ipn.escom.meteora.ui.agenda

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Subject
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DescriptionField(
    enabled: Boolean = true,
    eventDescription: String,
    onEventDescriptionChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.Subject,
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .size(24.dp)
                .align(Alignment.Top)
        )

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = eventDescription,
            onValueChange = { onEventDescriptionChanged(it) },
            placeholder = { Text(if(!enabled && eventDescription.isEmpty()) "Sin descripción" else "Descripción", style = MaterialTheme.typography.bodyMedium) },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Top),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledTextColor = MaterialTheme.colorScheme.onSurface, // Use the same text color
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant, // If you have leading icons
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant // If you have trailing icons
            ),
            textStyle = MaterialTheme.typography.bodyMedium,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            enabled = enabled,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DescriptionFieldPreview() {
    DescriptionField(eventDescription = "Descripción") {}
}
