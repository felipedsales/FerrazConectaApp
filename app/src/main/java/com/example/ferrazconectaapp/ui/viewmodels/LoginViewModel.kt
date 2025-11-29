package com.example.ferrazconectaapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface LoginUiState {
    object Idle : LoginUiState
    data class Success(val provider: String?) : LoginUiState // provider é nulo para login com e-mail
    data class Error(val message: String) : LoginUiState
}

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var passwordVisible by mutableStateOf(false)
        private set

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }

    fun onLoginClick() {
        if (email.isNotBlank() && password.isNotBlank()) {
            _uiState.value = LoginUiState.Success(null)
        } else {
            _uiState.value = LoginUiState.Error("E-mail e senha não podem estar em branco.")
        }
    }

    fun onSignInWithGoogleClick() {
        _uiState.value = LoginUiState.Success("Google")
    }

    fun onSignInWithLinkedInClick() {
        _uiState.value = LoginUiState.Success("LinkedIn")
    }

    fun resetLoginState() {
        _uiState.value = LoginUiState.Idle
    }
}
