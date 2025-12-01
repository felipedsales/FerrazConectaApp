package com.example.ferrazconectaapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ferrazconectaapp.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    object Idle : LoginUiState
    data class Success(val provider: String?) : LoginUiState // provider é nulo para login com e-mail
    data class Error(val message: String) : LoginUiState
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
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
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("E-mail e senha não podem estar em branco.")
            return
        }

        viewModelScope.launch {
            try {
                authRepository.login(email, password)
                _uiState.value = LoginUiState.Success(null)
            } catch (e: FirebaseAuthInvalidUserException) {
                _uiState.value = LoginUiState.Error("Usuário não encontrado.")
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _uiState.value = LoginUiState.Error("E-mail ou senha inválidos.")
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "Ocorreu um erro ao tentar fazer o login.")
            }
        }
    }

    fun onSignInWithGoogleClick() {
        // Lógica do Google Sign-In será implementada no futuro
        _uiState.value = LoginUiState.Error("Login com Google será implementado em breve.")
    }

    fun onSignInWithLinkedInClick() {
        // Lógica do LinkedIn Sign-In será implementada no futuro
        _uiState.value = LoginUiState.Error("Login com LinkedIn será implementado em breve.")
    }

    fun resetLoginState() {
        _uiState.value = LoginUiState.Idle
    }
}
