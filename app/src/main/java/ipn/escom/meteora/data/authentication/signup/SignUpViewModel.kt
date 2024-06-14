package ipn.escom.meteora.data.authentication.signup

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipn.escom.meteora.data.authentication.data.network.response.AuthenticationResponse
import ipn.escom.meteora.data.authentication.domain.AuthenticationUseCase
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
        validateFields(email, password, repeatPassword)
    }

    private fun validateFields(email: String, password: String, repeatPassword: String) {
        _errorMessage.value = ""

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorMessage.value = "El correo no es válido"
            _showError.value = true
            return
        }

        if (password.length < 8) {
            _errorMessage.value = "La contraseña debe tener al menos 8 caracteres"
            _showError.value = true
            return
        }

        if (!password.any { it.isUpperCase() }) {
            _errorMessage.value = "La contraseña debe tener al menos una mayúscula"
            _showError.value = true
            return
        }

        if (!password.any { it.isLowerCase() }) {
            _errorMessage.value = "La contraseña debe tener al menos una minúscula"
            _showError.value = true
            return
        }

        if (!password.any { it.isDigit() }) {
            _errorMessage.value = "La contraseña debe tener al menos un número"
            _showError.value = true
            return
        }

        if (!password.any { !it.isLetterOrDigit() }) {
            _errorMessage.value = "La contraseña debe tener al menos un carácter especial"
            _showError.value = true
            return
        }

        if (password != repeatPassword) {
            _errorMessage.value = "Las contraseñas no coinciden"
            _showError.value = true
            return
        }

        _showError.value = false
        _isSignUpEnabled.value = true
    }

    fun signUp(context: Context) {
        val username = _username.value.orEmpty()
        val email = _email.value.orEmpty()
        val password = _password.value.orEmpty()
        val selectedImageUri = _selectedImageUri.value

        _showSignUpIndicator.value = true

        viewModelScope.launch {
            val result =
                authenticationUseCase.signUp(context, username, email, password, selectedImageUri)
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
