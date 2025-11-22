package com.example.ferrazconectaapp.ui.viewmodels

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CadastroViewModel : ViewModel() {

    // Estado dos campos do formulário
    var nome by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var cpf by mutableStateOf("")
        private set
    var dataNascimento by mutableStateOf("")
        private set
    var telefone by mutableStateOf("")
        private set
    var senha by mutableStateOf("")
        private set
    var confirmarSenha by mutableStateOf("")
        private set

    // Estado de erro de validação
    var emailError by mutableStateOf<String?>(null)
        private set
    var confirmarSenhaError by mutableStateOf<String?>(null)
        private set

    // Estado de validade geral do formulário
    var isFormValid by mutableStateOf(false)
        private set

    private val _cadastroSuccess = MutableStateFlow(false)
    val cadastroSuccess = _cadastroSuccess.asStateFlow()

    fun onNomeChange(newName: String) {
        nome = newName
        validateForm()
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
        validateEmail()
        validateForm()
    }

    fun onCpfChange(newCpf: String) {
        if (newCpf.length <= 11) {
            cpf = newCpf.filter { it.isDigit() }
            validateForm()
        }
    }

    fun onDataNascimentoChange(newDate: String) {
        if (newDate.length <= 8) {
            dataNascimento = newDate.filter { it.isDigit() }
            validateForm()
        }
    }

    fun onTelefoneChange(newPhone: String) {
        if (newPhone.length <= 11) {
            telefone = newPhone.filter { it.isDigit() }
            validateForm()
        }
    }

    fun onSenhaChange(newPassword: String) {
        senha = newPassword
        validatePasswordConfirmation()
        validateForm()
    }

    fun onConfirmarSenhaChange(newPassword: String) {
        confirmarSenha = newPassword
        validatePasswordConfirmation()
        validateForm()
    }

    private fun validateEmail() {
        emailError = if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Formato de e-mail inválido"
        } else {
            null
        }
    }

    private fun validatePasswordConfirmation() {
        confirmarSenhaError = if (senha != confirmarSenha) {
            "As senhas não coincidem"
        } else {
            null
        }
    }

    private fun validateForm() {
        val isEmailFieldValid = email.isNotBlank() && emailError == null
        val arePasswordsValid = senha.isNotBlank() && confirmarSenha.isNotBlank() && confirmarSenhaError == null
        
        isFormValid = nome.isNotBlank() && 
                      cpf.length == 11 && 
                      dataNascimento.length == 8 && 
                      telefone.length == 11 && 
                      isEmailFieldValid && 
                      arePasswordsValid
    }

    fun onCadastroClick() {
        if(isFormValid) {
            _cadastroSuccess.value = true
        }
    }
}
