package ipn.escom.meteora.ui.login

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ipn.escom.meteora.R
import ipn.escom.meteora.data.authentication.signup.SignUpViewModel
import ipn.escom.meteora.ui.Screens

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SignUp1(navController: NavController? = null, signUpViewModel: SignUpViewModel) {

    val username: String by signUpViewModel.username.observeAsState(initial = "")
    val selectedImageUri: String? by signUpViewModel.selectedImageUri.observeAsState()

    val registerImageActivityLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            signUpViewModel.onImageSelected(uri.toString())
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Header(
            title = stringResource(id = R.string.sign_up),
            subtitle = stringResource(R.string.auth_subtitle)
        )

        Spacer(modifier = Modifier.height(36.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            if (selectedImageUri != null && selectedImageUri!!.isNotEmpty()) {
                GlideImage(
                    model = selectedImageUri,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                IconButton(
                    onClick = {
                        signUpViewModel.onImageSelected("")
                    },
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.TopEnd)
                        .background(MaterialTheme.colorScheme.secondary, CircleShape)
                        .padding(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.delete_picture),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .clickable {
                            registerImageActivityLauncher.launch("image/*")
                        }
                        .background(MaterialTheme.colorScheme.onBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AddPhotoAlternate,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        UserName(username) {
            signUpViewModel.onNameChanged(it)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            shape = MaterialTheme.shapes.small,
            onClick = {
                navController?.navigate(Screens.SignUp2.name)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp),
            enabled = username.isNotEmpty()
        ) {
            Text(
                text = stringResource(R.string.continue_action),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        OrDivider()

        GoogleSignInButton(navController)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSignUpScreen1() {
    SignUp1(navController = null, signUpViewModel = SignUpViewModel())
}
