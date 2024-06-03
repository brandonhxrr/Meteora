package ipn.escom.meteora.ui.login

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.WbCloudy
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import ipn.escom.meteora.R
import ipn.escom.meteora.ui.Screens

@Composable
fun Header(title: String, subtitle: String = "") {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )

        if (subtitle.isNotEmpty()) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun UserName(username: String, onTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = username,
        onValueChange = {
            onTextChanged(it)
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.user),
                style = MaterialTheme.typography.bodySmall
            )
        },
        textStyle = MaterialTheme.typography.bodySmall,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
    )
}

@Composable
fun Email(email: String, onTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = {
            onTextChanged(it)
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.email),
                style = MaterialTheme.typography.bodySmall
            )
        },
        textStyle = MaterialTheme.typography.bodySmall,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
    )
}

@Composable
fun Password(
    password: String,
    repeat: Boolean = false,
    final: Boolean = true,
    onTextChanged: (String) -> Unit
) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    OutlinedTextField(
        value = password,
        onValueChange = {
            onTextChanged(it)
        },
        singleLine = true,
        placeholder = {
            Text(
                text = if (repeat) stringResource(id = R.string.repeat_password) else stringResource(
                    id = R.string.password
                ),
                style = MaterialTheme.typography.bodySmall
            )
        },
        textStyle = MaterialTheme.typography.bodySmall,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = if (final) ImeAction.Done else ImeAction.Next
        ),
        visualTransformation =
        if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            IconButton(onClick = { passwordHidden = !passwordHidden }, content = {
                val visibilityIcon =
                    if (passwordHidden) Icons.Rounded.WbSunny else Icons.Rounded.WbCloudy
                val description = if (passwordHidden) "Show password" else "Hide password"
                Icon(imageVector = visibilityIcon, contentDescription = description)
            })
        },
    )
}

@Composable
fun OrDivider() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .align(
                    Alignment.CenterVertically
                )
        )
        Text(
            text = "o", modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            textAlign = TextAlign.Center

        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .align(
                    Alignment.CenterVertically
                )
        )
    }
}

@Composable
fun GoogleSignInButton(navController: NavController?) {
    val context = LocalContext.current
    val googleSignInClient: GoogleSignInClient = remember { provideGoogleSignInClient(context) }

    val registerSignInActivityLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
                navController?.navigate(Screens.Home.name) {
                    popUpTo(Screens.Login.name) {
                        inclusive = true
                    }
                }
            } catch (e: ApiException) {
                Log.w("", "Google sign in failed", e)
            }
        }

    OutlinedButton(
        onClick = {
            val signInIntent = googleSignInClient.signInIntent
            registerSignInActivityLauncher.launch(signInIntent)
        },
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .padding(16.dp)
            .height(50.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google Icon",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Sign in with Google",
            )
        }
    }

}

private fun provideGoogleSignInClient(context: Context): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    return GoogleSignIn.getClient(context, gso)
}

private fun firebaseAuthWithGoogle(idToken: String) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    val auth = FirebaseAuth.getInstance()
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("GSI", "signInWithCredential:success")
            } else {
                Log.w("GSI", "signInWithCredential:failure", task.exception)
            }
        }
}

@Composable
fun ErrorMessage(errorMessage: String) {
    if (errorMessage.isNotEmpty()) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .border(1.dp, Color(0xFFE57373), MaterialTheme.shapes.small),
                color = Color(0xFFFFDAD9),
                contentColor = Color(0xFFE57373)
            ) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun AlertMessage(message: String) {
    if (message.isNotEmpty()) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = "Info",
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = message,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

@Composable
private fun PasswordSecurityIndicator(passwordSecurity: PasswordSecurity) {
    val color = when (passwordSecurity) {
        PasswordSecurity.WEAK -> Color.Red
        PasswordSecurity.MODERATE -> Color.Yellow
        PasswordSecurity.STRONG -> Color.Green
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(color)
    )
}