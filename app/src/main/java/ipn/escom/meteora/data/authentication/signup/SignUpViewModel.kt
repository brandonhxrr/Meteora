package ipn.escom.meteora.data.authentication.signup

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipn.escom.meteora.R
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

    fun onSignUpChanged(context: Context, email: String, password: String, repeatPassword: String) {
        _email.value = email
        _password.value = password
        _repeatPassword.value = repeatPassword
        validateFields(context, email, password, repeatPassword)
    }

    private fun validateFields(
        context: Context,
        email: String,
        password: String,
        repeatPassword: String
    ) {
        _errorMessage.value = ""

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorMessage.value = context.getString(R.string.invalid_email)
            _showError.value = true
            return
        }

        if (password.length < 8) {
            _errorMessage.value = context.getString(R.string.invalid_password_length)
            _showError.value = true
            return
        }

        if (!password.any { it.isUpperCase() }) {
            _errorMessage.value = context.getString(R.string.password_caps_required)
            _showError.value = true
            return
        }

        if (!password.any { it.isLowerCase() }) {
            _errorMessage.value = context.getString(R.string.password_lower_case_required)
            _showError.value = true
            return
        }

        if (!password.any { it.isDigit() }) {
            _errorMessage.value = context.getString(R.string.password_number_required)
            _showError.value = true
            return
        }

        if (!password.any { !it.isLetterOrDigit() }) {
            _errorMessage.value = context.getString(R.string.password_special_character_required)
            _showError.value = true
            return
        }

        if (password != repeatPassword) {
            _errorMessage.value = context.getString(R.string.passwords_unmatch)
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
