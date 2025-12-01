package com.example.ferrazconectaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ferrazconectaapp.data.model.User
import com.example.ferrazconectaapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PerfilUiState {
    object Loading : PerfilUiState
    data class Success(val user: User) : PerfilUiState
    data class Error(val message: String) : PerfilUiState
}

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PerfilUiState>(PerfilUiState.Loading)
    val uiState: StateFlow<PerfilUiState> = _uiState

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        viewModelScope.launch {
            _uiState.value = PerfilUiState.Loading
            val currentUser = authRepository.getCurrentUser()
            if (currentUser != null) {
                try {
                    val user = authRepository.getUser(currentUser.uid)
                    if (user != null) {
                        _uiState.value = PerfilUiState.Success(user)
                    } else {
                        _uiState.value = PerfilUiState.Error("Usuário não encontrado no banco de dados.")
                    }
                } catch (e: Exception) {
                    _uiState.value = PerfilUiState.Error(e.message ?: "Ocorreu um erro ao buscar os dados do usuário.")
                }
            } else {
                _uiState.value = PerfilUiState.Error("Nenhum usuário logado.")
            }
        }
    }
}
