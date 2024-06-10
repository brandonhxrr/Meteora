package ipn.escom.meteora.data.authentication.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ipn.escom.meteora.data.authentication.data.network.response.AuthenticationResponse
import ipn.escom.meteora.data.authentication.domain.AuthenticationUseCase
import kotlinx.coroutines.launch

open class LoginViewModel : ViewModel() {

    private val authenticationUseCase = AuthenticationUseCase()

    private val _email = MutableLiveData<String>()
    open val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    open val password: LiveData<String> = _password

    private val _isLoginEnabled = MutableLiveData<Boolean>()
    open val isLoginEnabled: LiveData<Boolean> = _isLoginEnabled

    private val _errorMessage = MutableLiveData<String>()
    open val errorMessage: LiveData<String> = _errorMessage

    private val _signInSuccess = MutableLiveData<Boolean>()
    open val signInSuccess: LiveData<Boolean> = _signInSuccess

    private val _errorCounter = MutableLiveData<Int>()
    open val errorCounter: LiveData<Int> = _errorCounter

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _isLoginEnabled.value = enableLogin(email, password)
    }

    private fun enableLogin(email: String, password: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 8

    open fun signIn() {
        val email = _email.value.orEmpty()
        val password = _password.value.orEmpty()

        viewModelScope.launch {
            val result = authenticationUseCase.signIn(email, password)

            if (result is AuthenticationResponse.Success) {
                _signInSuccess.value = true
            } else if (result is AuthenticationResponse.Error) {
                _signInSuccess.value = false
                _errorMessage.value = result.message
                _errorCounter.value = (_errorCounter.value ?: 0) + 1
            }
        }
    }

}