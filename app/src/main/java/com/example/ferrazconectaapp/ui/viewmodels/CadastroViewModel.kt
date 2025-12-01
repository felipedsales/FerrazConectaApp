package com.example.ferrazconectaapp.ui.viewmodels

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ferrazconectaapp.data.model.User
import com.example.ferrazconectaapp.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CadastroUiState {
    object Idle : CadastroUiState
    object Success : CadastroUiState
    data class Error(val message: String) : CadastroUiState
}

data class FormFieldState(val value: String = "", var error: String? = null)

@HiltViewModel
class CadastroViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var nome by mutableStateOf(FormFieldState())
        private set
    var email by mutableStateOf(FormFieldState())
        private set
    var cpf by mutableStateOf(FormFieldState())
        private set
    var dataNascimento by mutableStateOf(FormFieldState())
        private set
    var telefone by mutableStateOf(FormFieldState())
        private set
    var senha by mutableStateOf(FormFieldState())
        private set
    var confirmarSenha by mutableStateOf(FormFieldState())
        private set

    private val _uiState = MutableStateFlow<CadastroUiState>(CadastroUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun onNomeChange(newValue: String) {
        nome = nome.copy(value = newValue)
    }

    fun onEmailChange(newValue: String) {
        email = email.copy(value = newValue)
    }

    fun onCpfChange(newValue: String) {
        if (newValue.length <= 11) {
            cpf = cpf.copy(value = newValue.filter { it.isDigit() })
        }
    }

    fun onDataNascimentoChange(newValue: String) {
        if (newValue.length <= 8) {
            dataNascimento = dataNascimento.copy(value = newValue.filter { it.isDigit() })
        }
    }

    fun onTelefoneChange(newValue: String) {
        if (newValue.length <= 11) {
            telefone = telefone.copy(value = newValue.filter { it.isDigit() })
        }
    }

    fun onSenhaChange(newValue: String) {
        senha = senha.copy(value = newValue)
        validatePasswordConfirmation()
    }

    fun onConfirmarSenhaChange(newValue: String) {
        confirmarSenha = confirmarSenha.copy(value = newValue)
        validatePasswordConfirmation()
    }

    fun validateNome() {
        nome = nome.copy(error = if (nome.value.isBlank()) "Nome não pode estar em branco" else null)
    }

    fun validateEmail() {
        val error = when {
            email.value.isBlank() -> "E-mail não pode estar em branco"
            !Patterns.EMAIL_ADDRESS.matcher(email.value).matches() -> "Formato de e-mail inválido"
            else -> null
        }
        email = email.copy(error = error)
    }

    fun validateCpf() {
        cpf = cpf.copy(error = if (cpf.value.length != 11) "CPF deve ter 11 dígitos" else null)
    }

    fun validateDataNascimento() {
        dataNascimento = dataNascimento.copy(error = if (dataNascimento.value.length != 8) "Data deve ter 8 dígitos" else null)
    }

    fun validateTelefone() {
        telefone = telefone.copy(error = if (telefone.value.length < 10) "Telefone inválido" else null)
    }

    fun validateSenha() {
        senha = senha.copy(error = if (senha.value.length < 6) "Senha deve ter no mínimo 6 caracteres" else null)
        validatePasswordConfirmation()
    }

    private fun validatePasswordConfirmation() {
        val error = if (senha.value != confirmarSenha.value) "As senhas não coincidem" else null
        confirmarSenha = confirmarSenha.copy(error = error)
    }

    fun onCadastroClick() {
        validateAllFields()
        if (isFormValid()) {
            viewModelScope.launch {
                try {
                    val uid = authRepository.register(email.value, senha.value)
                    val newUser = User(
                        uid = uid,
                        nome = nome.value,
                        email = email.value,
                        cpf = cpf.value,
                        dataNascimento = dataNascimento.value,
                        telefone = telefone.value
                    )
                    authRepository.createUser(newUser)
                    _uiState.value = CadastroUiState.Success
                } catch (e: FirebaseAuthUserCollisionException) {
                    _uiState.value = CadastroUiState.Error("Este e-mail já está em uso.")
                } catch (e: FirebaseAuthWeakPasswordException) {
                    _uiState.value = CadastroUiState.Error("A senha é muito fraca. A senha deve ter no mínimo 6 caracteres.")
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    _uiState.value = CadastroUiState.Error("O formato do e-mail é inválido.")
                } catch (e: Exception) {
                    _uiState.value = CadastroUiState.Error(e.message ?: "Ocorreu um erro durante o cadastro.")
                }
            }
        } else {
            _uiState.value = CadastroUiState.Error("Por favor, corrija os erros no formulário.")
        }
    }

    fun signInWithGoogleToken(idToken: String) {
        viewModelScope.launch {
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                authRepository.firebaseSignInWithGoogle(credential)
                _uiState.value = CadastroUiState.Success
            } catch (e: Exception) {
                _uiState.value = CadastroUiState.Error(e.message ?: "Ocorreu um erro com o login.")
            }
        }
    }

    private fun validateAllFields() {
        validateNome()
        validateEmail()
        validateCpf()
        validateDataNascimento()
        validateTelefone()
        validateSenha()
        validatePasswordConfirmation()
    }

    private fun isFormValid(): Boolean {
        return nome.error == null && email.error == null && cpf.error == null &&
               dataNascimento.error == null && telefone.error == null &&
               senha.error == null && confirmarSenha.error == null
    }

    fun resetUiState() {
        _uiState.value = CadastroUiState.Idle
    }
}
