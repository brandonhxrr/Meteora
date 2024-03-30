package ipn.escom.meteora.ui.login

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
fun UserName(username: String, onTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = username,
        onValueChange = {
            onTextChanged(it)
        },
        label = { Text(text = stringResource(id = R.string.user)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        leadingIcon = {
            Icon(
                Icons.Rounded.Person,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    )
}

@Composable
fun Email(email: String, onTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = {
            onTextChanged(it)
        },
        label = { Text(text = stringResource(id = R.string.email)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        leadingIcon = {
            Icon(
                Icons.Rounded.Person,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    )
}

@Composable
fun Password(password: String, repeat: Boolean = false, final: Boolean = true, onTextChanged: (String) -> Unit) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    OutlinedTextField(
        value = password,
        onValueChange = {
            onTextChanged(it)
        },
        singleLine = true,
        label = { Text(text = if (repeat) stringResource(id = R.string.repeat_password) else stringResource(id = R.string.password)) },
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
                    if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordHidden) "Show password" else "Hide password"
                Icon(imageVector = visibilityIcon, contentDescription = description)
            })
        },
        leadingIcon = { Icon(Icons.Rounded.Lock, contentDescription = null, tint = Color.Gray) }
    )
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

    Button(
        onClick = {
            val signInIntent = googleSignInClient.signInIntent
            registerSignInActivityLauncher.launch(signInIntent)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .padding(16.dp)
            .height(50.dp)
            .fillMaxWidth()
    ) {
        Text(text = "Sign in with Google")
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
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
        ) {

            Text(
                text = errorMessage,
                modifier = Modifier.padding(8.dp),
                fontSize = 12.sp

            )
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