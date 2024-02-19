package ipn.escom.meteora.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import ipn.escom.meteora.R
import java.io.ByteArrayOutputStream

@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalGlideComposeApi::class
)
@Composable
fun SignUp(navController: NavController? = null) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var user by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    var repeatPassword by rememberSaveable { mutableStateOf("") }
    var repeatPasswordHidden by rememberSaveable { mutableStateOf(true) }
    var selectedImageUri by remember { mutableStateOf<String?>(null) }
    var userName by remember { mutableStateOf("") }
    var showSignUpIndicator by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val auth = FirebaseAuth.getInstance()
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference

    var errorText by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    val registerImageActivityLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri.toString()
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .align(alignment = Alignment.CenterVertically),
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight(400)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.sign_up),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(alignment = Alignment.CenterHorizontally),
            color = Color.Black,
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .clickable {
                registerImageActivityLauncher.launch("image/*")
            }
            .align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                GlideImage(
                    model = selectedImageUri,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.AddAPhoto,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .background(MaterialTheme.colorScheme.onBackground)
                        .padding(20.dp),
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text(stringResource(id = R.string.user)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
            )
        )

        OutlinedTextField(value = user,
            onValueChange = { user = it },
            label = { Text(text = stringResource(id = R.string.email)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
            ),
            leadingIcon = {
                Icon(
                    Icons.Rounded.Mail, contentDescription = null, tint = Color.Gray
                )
            })

        OutlinedTextField(value = password,
            onValueChange = {
                password = it
            },
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.password)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
            ),
            visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                IconButton(onClick = { passwordHidden = !passwordHidden }, content = {
                    val visibilityIcon =
                        if (passwordHidden) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff
                    val description = if (passwordHidden) "Show password" else "Hide password"
                    Icon(imageVector = visibilityIcon, contentDescription = description)
                })
            },
            leadingIcon = {
                Icon(
                    Icons.Rounded.Lock, contentDescription = null, tint = Color.Gray
                )
            })
        OutlinedTextField(value = repeatPassword,
            onValueChange = { repeatPassword = it },
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.repeat_password)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            visualTransformation = if (repeatPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                IconButton(onClick = { repeatPasswordHidden = !repeatPasswordHidden }, content = {
                    val visibilityIcon =
                        if (repeatPasswordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (repeatPasswordHidden) "Show password" else "Hide password"
                    Icon(imageVector = visibilityIcon, contentDescription = description)
                })
            },
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                keyboardController?.hide()
            }),
            leadingIcon = {
                Icon(
                    Icons.Rounded.Lock, contentDescription = null, tint = Color.Gray
                )
            })

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (validateInputs(user, password, repeatPassword, context)) {
                    keyboardController?.hide()

                    auth.createUserWithEmailAndPassword(user, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userId = auth.currentUser?.uid

                                if (selectedImageUri != null) {
                                    Glide.with(context)
                                        .asBitmap()
                                        .load(selectedImageUri)
                                        .override(300, 300)
                                        .into(object : CustomTarget<Bitmap>() {
                                            override fun onResourceReady(
                                                resource: Bitmap,
                                                transition: Transition<in Bitmap>?
                                            ) {
                                                val baos = ByteArrayOutputStream()
                                                resource.compress(
                                                    Bitmap.CompressFormat.JPEG,
                                                    100,
                                                    baos
                                                )
                                                val imageData = baos.toByteArray()

                                                val imageRef =
                                                    storageRef.child("profile_images/$userId")
                                                val uploadTask = imageRef.putBytes(imageData)

                                                uploadTask.continueWithTask { task ->
                                                    if (!task.isSuccessful) {
                                                        task.exception?.let {
                                                            throw it
                                                        }
                                                    }
                                                    imageRef.downloadUrl
                                                }.addOnCompleteListener { downloadUrlTask ->
                                                    if (downloadUrlTask.isSuccessful) {
                                                        val profileUpdates =
                                                            UserProfileChangeRequest.Builder()
                                                                .setDisplayName(userName)
                                                                .setPhotoUri(downloadUrlTask.result)
                                                                .build()

                                                        auth.currentUser?.updateProfile(
                                                            profileUpdates
                                                        )

                                                        val userReference =
                                                            FirebaseDatabase.getInstance()
                                                                .getReference("users")
                                                                .child(userId.orEmpty())

                                                        val userData = hashMapOf(
                                                            "name" to userName,
                                                            "email" to user,
                                                            "profileImageUrl" to downloadUrlTask.result.toString()
                                                        )

                                                        userReference.setValue(userData)
                                                            .addOnSuccessListener {
                                                                showSignUpIndicator = false
                                                                navController?.navigate(Screens.Home.name) {
                                                                    popUpTo(Screens.SignUp.name) {
                                                                        inclusive = true
                                                                    }
                                                                }
                                                            }
                                                            .addOnFailureListener {
                                                                showSignUpIndicator = false
                                                            }
                                                    } else {
                                                        // Manejar error al obtener la URL de la imagen
                                                    }
                                                }
                                            }

                                            override fun onLoadCleared(placeholder: Drawable?) {
                                                TODO("Not yet implemented")
                                            }
                                        })

                                } else {
                                    val userReference = FirebaseDatabase.getInstance()
                                        .getReference("users").child(userId.orEmpty())

                                    val userData = hashMapOf(
                                        "name" to userName,
                                        "email" to user,
                                        "profileImageUrl" to ""
                                    )

                                    userReference.setValue(userData)
                                        .addOnSuccessListener {
                                            showSignUpIndicator = false
                                            navController?.navigate(Screens.Home.name) {
                                                popUpTo(Screens.SignUp.name) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                        .addOnFailureListener {
                                            showSignUpIndicator = false
                                        }
                                }
                            } else {
                                errorText = try {
                                    throw task.exception!!
                                } catch (e: FirebaseAuthUserCollisionException) {
                                    // Email ya registrado
                                    showSignUpIndicator = false
                                    "Correo ya registrado"
                                } catch (e: Exception) {
                                    showSignUpIndicator = false
                                    // Otros errores de Firebase
                                    "Error en el registro"
                                }

                                Toast.makeText(
                                    context,
                                    errorText,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            modifier = Modifier
                .height(50.dp)
                .align(alignment = Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(id = R.string.sign_up))
        }



        Spacer(modifier = Modifier.height(50.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(id = R.string.yes_account),
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .padding(end = 8.dp),
                fontFamily = FontFamily(Font(R.font.product_sans_regular))
            )
            Text(
                text = stringResource(id = R.string.login_action),
                color = Color.Blue,
                modifier = Modifier.clickable {
                    navController?.navigate(Screens.Login.name) {
                        popUpTo(Screens.SignUp.name) {
                            inclusive = true
                        }
                    }
                },
                fontFamily = FontFamily(Font(R.font.product_sans_regular))
            )
        }

        if (showSignUpIndicator) {
            AlertDialog(
                onDismissRequest = {
                },
                title = {
                    Text(text = "Registrando usuario")
                },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.PersonAdd,
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("No salgas de la aplicación")
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {

                }
            )
        }
    }
}

enum class PasswordSecurity {
    NONE, WEAK, MODERATE, STRONG
}

private fun evaluatePasswordSecurity(password: String): PasswordSecurity {
    val minLength = 8
    val hasUppercase = password.any { it.isUpperCase() }
    val hasLowercase = password.any { it.isLowerCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecialChar = password.any { !it.isLetterOrDigit() }

    return when {
        password.length < minLength -> PasswordSecurity.NONE
        !hasUppercase && !hasLowercase && !hasDigit && !hasSpecialChar -> PasswordSecurity.WEAK
        (hasUppercase || hasLowercase) && hasDigit && hasSpecialChar -> PasswordSecurity.STRONG
        else -> PasswordSecurity.MODERATE
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

private fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}


private fun validateInputs(
    user: String,
    password: String,
    repeatPassword: String,
    context: Context
): Boolean {
    var isValid = true
    var errorMessage = ""

    if (user.isBlank() || !isValidEmail(user)) {
        errorMessage = "El correo no es válido"
        isValid = false
    } else if (password.isBlank()) {
        errorMessage = "La contraseña no puede estar vacía"
        isValid = false
    } else if (password != repeatPassword) {
        errorMessage = "Las contraseñas no coinciden"
        isValid = false
    } else if (evaluatePasswordSecurity(password) != PasswordSecurity.STRONG) {
        isValid = false
        errorMessage =
            "La contraseña debe ser de al menos 8 caracteres, tener al menos una mayúscula, una minúscula, un número y un caracter especial"
    }

    if (!isValid) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    return isValid
}

@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    SignUp()
}