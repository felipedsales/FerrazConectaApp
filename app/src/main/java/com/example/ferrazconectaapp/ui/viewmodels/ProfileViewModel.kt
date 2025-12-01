package com.example.ferrazconectaapp.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ferrazconectaapp.data.model.Escolaridade
import com.example.ferrazconectaapp.data.model.ExperienciaProfissional
import com.example.ferrazconectaapp.data.model.User
import com.example.ferrazconectaapp.data.repository.AuthRepository
import com.example.ferrazconectaapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProfileUiState {
    object Loading : ProfileUiState
    data class Success(
        val user: User,
        val isEditing: Boolean = false,
        val resumeUri: Uri? = null, // Manter o gerenciamento de URI do currículo
        val showSaveConfirmation: Boolean = false
    ) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            authRepository.getCurrentUser()?.uid?.let {
                try {
                    val user = authRepository.getUser(it)
                    if (user != null) {
                        _uiState.value = ProfileUiState.Success(user = user)
                    } else {
                        _uiState.value = ProfileUiState.Error("Usuário não encontrado.")
                    }
                } catch (e: Exception) {
                    _uiState.value = ProfileUiState.Error(e.message ?: "Erro ao buscar perfil.")
                }
            } ?: run {
                _uiState.value = ProfileUiState.Error("Nenhum usuário logado.")
            }
        }
    }

    fun deleteEscolaridade(escolaridade: Escolaridade) {
        viewModelScope.launch {
            try {
                userRepository.deleteEscolaridade(escolaridade)
                fetchUserProfile() // Recarrega os dados para atualizar a UI
            } catch (e: Exception) {
                // Tratar erro, talvez com um novo estado de erro no UI
            }
        }
    }

    fun deleteExperienciaProfissional(experiencia: ExperienciaProfissional) {
        viewModelScope.launch {
            try {
                userRepository.deleteExperienciaProfissional(experiencia)
                fetchUserProfile() // Recarrega os dados para atualizar a UI
            } catch (e: Exception) {
                // Tratar erro
            }
        }
    }

    fun onNomeChange(nome: String) {
        (_uiState.value as? ProfileUiState.Success)?.let { currentState ->
            _uiState.update { currentState.copy(user = currentState.user.copy(nome = nome)) }
        }
    }

    fun onEmailChange(email: String) {
        // Geralmente o e-mail não é editável, mas mantendo a lógica se necessário.
        (_uiState.value as? ProfileUiState.Success)?.let { currentState ->
            _uiState.update { currentState.copy(user = currentState.user.copy(email = email)) }
        }
    }

    fun onTelefoneChange(telefone: String) {
        (_uiState.value as? ProfileUiState.Success)?.let { currentState ->
            _uiState.update { currentState.copy(user = currentState.user.copy(telefone = telefone)) }
        }
    }

    fun onCpfChange(cpf: String) {
        (_uiState.value as? ProfileUiState.Success)?.let { currentState ->
            _uiState.update { currentState.copy(user = currentState.user.copy(cpf = cpf)) }
        }
    }

    fun onDataNascimentoChange(dataNascimento: String) {
        (_uiState.value as? ProfileUiState.Success)?.let { currentState ->
            _uiState.update { currentState.copy(user = currentState.user.copy(dataNascimento = dataNascimento)) }
        }
    }
    
    fun onEnderecoChange(endereco: String) {
        (_uiState.value as? ProfileUiState.Success)?.let { currentState ->
            _uiState.update { currentState.copy(user = currentState.user.copy(endereco = endereco)) }
        }
    }

    fun onResumoProfissionalChange(resumo: String) {
        (_uiState.value as? ProfileUiState.Success)?.let { currentState ->
            _uiState.update { currentState.copy(user = currentState.user.copy(resumoProfissional = resumo)) }
        }
    }

    fun onResumeUriChange(uri: Uri?) {
         (_uiState.value as? ProfileUiState.Success)?.let { currentState ->
            _uiState.update { currentState.copy(resumeUri = uri) }
        }
    }

    fun onSaveClick() {
        viewModelScope.launch {
            (_uiState.value as? ProfileUiState.Success)?.let { currentState ->
                try {
                    // TODO: Adicionar lógica para upload do currículo (resumeUri)
                    authRepository.createUser(currentState.user) // Usando createUser como upsert
                    _uiState.update { currentState.copy(isEditing = false, showSaveConfirmation = true) }
                } catch (e: Exception) {
                     _uiState.value = ProfileUiState.Error(e.message ?: "Erro ao salvar perfil.")
                }
            }
        }
    }

    fun onToggleEdit() {
        (_uiState.value as? ProfileUiState.Success)?.let { currentState ->
             _uiState.update { currentState.copy(isEditing = !currentState.isEditing) }
        }
    }

    fun onSaveConfirmationShown() {
        (_uiState.value as? ProfileUiState.Success)?.let { currentState ->
            _uiState.update { currentState.copy(showSaveConfirmation = false) }
        }
    }
}
