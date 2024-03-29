package ipn.escom.meteora.data.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ipn.escom.meteora.ui.login.PasswordSecurity

class SignUpViewModel : ViewModel() {

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

    fun onImageSelected(imageUri: String) {
        _selectedImageUri.value = imageUri
    }

    fun onSignUpChanged(username: String, email: String, password: String, repeatPassword: String) {
        _username.value = username
        _email.value = email
        _password.value = password
        _repeatPassword.value = repeatPassword
        _isSignUpEnabled.value = enableSignUp(username, email, password, repeatPassword)
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
        username: String,
        email: String,
        password: String,
        repeatPassword: String
    ): Boolean {
        if (username.isBlank()) {
            _errorMessage.value = "El nombre de usuario no puede estar vacío"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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

        return validateSignUp(username, email, password, repeatPassword)
    }

    private fun validateSignUp(
        username: String,
        email: String,
        password: String,
        repeatPassword: String
    ) =
        username.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
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
}