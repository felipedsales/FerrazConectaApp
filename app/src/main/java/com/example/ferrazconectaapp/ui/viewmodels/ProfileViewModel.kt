package com.example.ferrazconectaapp.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val nome: String = "Yuris Moreira",
    val email: String = "yuris.moreira@example.com",
    val telefone: String = "(11) 99999-9999",
    val cpf: String = "123.456.789-00",
    val endereco: String = "Rua das Flores, 123, Ferraz de Vasconcelos - SP",
    val resumoProfissional: String = "Desenvolvedor Android com 5 anos de experiência...",
    val isEditing: Boolean = false,
    val resumeUri: Uri? = null,
    val showSaveConfirmation: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun onNomeChange(nome: String) {
        _uiState.update { it.copy(nome = nome) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onTelefoneChange(telefone: String) {
        _uiState.update { it.copy(telefone = telefone) }
    }

    fun onCpfChange(cpf: String) {
        _uiState.update { it.copy(cpf = cpf) }
    }

    fun onEnderecoChange(endereco: String) {
        _uiState.update { it.copy(endereco = endereco) }
    }

    fun onResumoProfissionalChange(resumo: String) {
        _uiState.update { it.copy(resumoProfissional = resumo) }
    }

    fun onResumeUriChange(uri: Uri?) {
        _uiState.update { it.copy(resumeUri = uri) }
    }

    fun onSaveClick() {
        viewModelScope.launch {
            // TODO: Adicionar lógica para salvar os dados no repositório/banco de dados
            _uiState.update { it.copy(isEditing = false, showSaveConfirmation = true) }
        }
    }

    fun onToggleEdit() {
        _uiState.update { it.copy(isEditing = !it.isEditing) }
    }
    
    fun onSaveConfirmationShown() {
        _uiState.update { it.copy(showSaveConfirmation = false) }
    }
}
