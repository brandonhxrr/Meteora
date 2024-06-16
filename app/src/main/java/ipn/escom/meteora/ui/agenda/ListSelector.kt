package ipn.escom.meteora.ui.agenda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.firebase.auth.FirebaseAuth
import ipn.escom.meteora.R
import ipn.escom.meteora.ui.theme.amber

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ListSelector() {
    val auth = FirebaseAuth.getInstance()
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        if (auth.currentUser?.photoUrl != null) {
            GlideImage(
                model = auth.currentUser?.photoUrl,
                contentDescription = stringResource(R.string.user_profile_picture_description),
                modifier = Modifier
                    .clip(
                        CircleShape
                    )
                    .size(40.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = stringResource(R.string.user_profile_picture_description)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Row {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(10.dp)
                        .background(amber)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.events),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = auth.currentUser?.email ?: "", style = MaterialTheme.typography.bodySmall
            )
        }
    }
}