package ipn.escom.meteora.ui.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import ipn.escom.meteora.R
import ipn.escom.meteora.data.login.LoginViewModel
import ipn.escom.meteora.ui.Screens

@Composable
fun Login(navController: NavController? = null, loginViewModel: LoginViewModel) {

    val email: String by loginViewModel.email.observeAsState(initial = "")
    val password: String by loginViewModel.password.observeAsState(initial = "")
    val isLoginEnabled: Boolean by loginViewModel.isLoginEnabled.observeAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Spacer(modifier = Modifier.height(72.dp))

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
                    .align(alignment = Alignment.CenterVertically),
                color = Color.Black,
                fontSize = 32.sp,
                fontWeight = FontWeight(400)
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = stringResource(id = R.string.login),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(alignment = Alignment.CenterHorizontally),
            color = Color.Black,
            fontSize = 40.sp
        )

        Spacer(modifier = Modifier.height(50.dp))

        Email(email) {
            loginViewModel.onLoginChanged(it, password)
        }

        Password(password = password, repeat = false, final = true) {
            loginViewModel.onLoginChanged(email, it)
        }

        Spacer(modifier = Modifier.height(24.dp))

        LoginButton(email, password, isLoginEnabled, navController)

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(id = R.string.no_account),
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .padding(end = 8.dp),
                fontFamily = FontFamily(Font(R.font.product_sans_regular))
            )
            Text(
                text = stringResource(id = R.string.sign_up),
                color = Color.Blue,
                modifier = Modifier.clickable {
                    navController?.navigate(Screens.SignUp.name) {
                        popUpTo(Screens.Login.name) {
                            inclusive = true
                        }
                    }
                },
                fontFamily = FontFamily(Font(R.font.product_sans_regular))
            )
        }
    }
}

@Composable
fun LoginButton(
    email: String,
    password: String,
    loginEnabled: Boolean,
    navController: NavController?
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    Button(
        onClick = {
            keyboardController?.hide()

            loginWithFirebase(email, password, context) {
                navController?.navigate(Screens.Home.name) {
                    popUpTo(Screens.Login.name) {
                        inclusive = true
                    }
                }
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = Modifier
            .padding(16.dp)
            .height(50.dp)
            .fillMaxWidth(),
        enabled = loginEnabled
    ) {
        Text(text = stringResource(id = R.string.login))
    }

}


fun loginWithFirebase(
    email: String,
    password: String,
    context: Context,
    onLoginSuccess: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onLoginSuccess()
            } else {
                val errorText = try {
                    throw task.exception!!
                } catch (e: FirebaseAuthInvalidUserException) {
                    "Usuario no encontrado"
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    "Credenciales inválidas"
                } catch (e: Exception) {
                    "Error en la autenticación"
                }
                Toast.makeText(
                    context,
                    errorText,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Login(null, LoginViewModel())
}
