package ipn.escom.meteora.ui.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ipn.escom.meteora.R
import ipn.escom.meteora.data.authentication.login.LoginViewModel
import ipn.escom.meteora.ui.Screens

@Composable
fun Login(navController: NavController? = null, loginViewModel: LoginViewModel) {

    val email: String by loginViewModel.email.observeAsState(initial = "")
    val password: String by loginViewModel.password.observeAsState(initial = "")
    val isLoginEnabled: Boolean by loginViewModel.isLoginEnabled.observeAsState(initial = false)
    val signInSuccess: Boolean by loginViewModel.signInSuccess.observeAsState(initial = false)
    val errorMessage: String by loginViewModel.errorMessage.observeAsState(initial = "")
    val errorCounter: Int by loginViewModel.errorCounter.observeAsState(initial = 0)
    val context = LocalContext.current

    LaunchedEffect(key1 = signInSuccess, block = {
        if (signInSuccess) {
            navController?.navigate(Screens.Home.name) {
                popUpTo(Screens.Login.name) {
                    inclusive = true
                }
            }
        }
    })

    LaunchedEffect(key1 = errorCounter, block = {
        if (errorMessage.isNotEmpty()) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    })


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Header(title = stringResource(id = R.string.login))

        Spacer(modifier = Modifier.height(40.dp))

        Email(email) {
            loginViewModel.onLoginChanged(it, password)
        }

        Password(password = password, repeat = false, final = true) {
            loginViewModel.onLoginChanged(email, it)
        }

        Spacer(modifier = Modifier.height(24.dp))

        LoginButton(isLoginEnabled, loginViewModel)

        OrDivider()

        GoogleSignInButton(navController)

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(id = R.string.no_account),
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .padding(end = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = stringResource(id = R.string.sign_up),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController?.navigate(Screens.SignUp1.name)
                },
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun LoginButton(
    loginEnabled: Boolean,
    loginViewModel: LoginViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Button(
        onClick = {
            keyboardController?.hide()
            loginViewModel.signIn()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(16.dp)
            .height(50.dp)
            .fillMaxWidth(),
        enabled = loginEnabled
    ) {
        Text(
            text = stringResource(id = R.string.login),
            style = MaterialTheme.typography.bodyMedium
        )
    }

}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Login(null, LoginViewModel())
}
