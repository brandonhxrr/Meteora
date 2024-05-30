package ipn.escom.meteora.data.authentication.signup

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipn.escom.meteora.data.authentication.data.network.response.AuthenticationResponse
import ipn.escom.meteora.data.authentication.domain.AuthenticationUseCase
import ipn.escom.meteora.ui.login.PasswordSecurity
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    private val authenticationUseCase = AuthenticationUseCase()

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _repeatPassword = MutableLiveData<String>()
    val repeatPassword: LiveData<String> = _repeatPassword

    private val _selectedImageUri = MutableLiveData<String>()
    val selectedImageUri: LiveData<String> = _selectedImageUri

    private val _isSignUpEnabled = MutableLiveData<Boolean>()
    val isSignUpEnabled: LiveData<Boolean> = _isSignUpEnabled

    private val _showSignUpIndicator = MutableLiveData<Boolean>()
    val showSignUpIndicator: LiveData<Boolean> = _showSignUpIndicator

    private val _showError = MutableLiveData<Boolean>()
    val showError: LiveData<Boolean> = _showError

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _signUpSuccess = MutableLiveData<Boolean>()
    val signUpSuccess: LiveData<Boolean> = _signUpSuccess

    fun onImageSelected(imageUri: String) {
        _selectedImageUri.value = imageUri
    }

    fun onNameChanged(username: String) {
        _username.value = username
    }

    fun onSignUpChanged(email: String, password: String, repeatPassword: String) {
        _email.value = email
        _password.value = password
        _repeatPassword.value = repeatPassword
        _isSignUpEnabled.value = enableSignUp(email, password, repeatPassword)
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

    private fun enableSignUp(
        email: String,
        password: String,
        repeatPassword: String
    ): Boolean {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorMessage.value = "El correo no es válido"
        } else if (password.isBlank()) {
            _errorMessage.value = "La contraseña no puede estar vacía"
        } else if (password != repeatPassword) {
            _errorMessage.value = "Las contraseñas no coinciden"
        } else if (evaluatePasswordSecurity(password) != PasswordSecurity.STRONG) {
            _errorMessage.value =
                "La contraseña debe ser de al menos 8 caracteres, tener al menos una mayúscula, una minúscula, un número y un caracter especial"
        } else {
            _errorMessage.value = ""
        }

        return validateSignUp(email, password, repeatPassword)
    }

    private fun validateSignUp(
        email: String,
        password: String,
        repeatPassword: String
    ) =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email)
            .matches() && password.length >= 8 && password == repeatPassword && evaluatePasswordSecurity(
            password
        ) == PasswordSecurity.STRONG

    fun enableSignUpIndicator() {
        _showSignUpIndicator.value = true
    }

    fun disableSignUpIndicator() {
        _showSignUpIndicator.value = false
    }

    fun showError() {
        _showError.value = true
    }

    fun hideError() {
        _showError.value = false
    }

    fun signUp(context: Context){
        val username = _username.value.orEmpty()
        val email = _email.value.orEmpty()
        val password = _password.value.orEmpty()
        val selectedImageUri = _selectedImageUri.value

        _showSignUpIndicator.value = true

        viewModelScope.launch{
            val result = authenticationUseCase.signUp(context, username, email, password, selectedImageUri)
            _showSignUpIndicator.value = false
            if (result is AuthenticationResponse.Success) {
                _showError.value = false
                _signUpSuccess.value = true
            } else if (result is AuthenticationResponse.Error) {
                _errorMessage.value = result.message
                _showError.value = true
                _signUpSuccess.value = false
            }
        }
    }
}