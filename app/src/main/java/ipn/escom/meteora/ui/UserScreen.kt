package ipn.escom.meteora.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserScreen(navController: NavController? = null) {
    val context = LocalContext.current
    val alertMessage = remember { mutableStateOf("") }
    var alertIcon = remember { mutableStateOf("") }
    val currentUser = FirebaseAuth.getInstance().currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Row {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        navController!!.navigateUp()
                    }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Perfil",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        GlideImage(
            model = currentUser?.photoUrl,
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = currentUser?.displayName ?: "",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = currentUser?.email ?: "",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                Firebase.auth.signOut()
                navController!!.navigate(Screens.Login.name) {
                    popUpTo(Screens.Home.name) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.AutoMirrored.Rounded.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Cerrar sesión", style = MaterialTheme.typography.bodyMedium)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UserScreenPreview() {
    UserScreen()
}
