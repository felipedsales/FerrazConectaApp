package com.example.ferrazconectaapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ferrazconectaapp.data.model.Vaga
import com.example.ferrazconectaapp.data.repository.CandidaturaRepository
import com.example.ferrazconectaapp.data.repository.VagaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

sealed interface VagaDetailsUiState {
    object Loading : VagaDetailsUiState
    data class Success(val vaga: Vaga, val isCandidatado: Boolean) : VagaDetailsUiState
    object Error : VagaDetailsUiState
}

@HiltViewModel
class VagaDetailsViewModel @Inject constructor(
    vagaRepository: VagaRepository,
    private val candidaturaRepository: CandidaturaRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<VagaDetailsUiState>(VagaDetailsUiState.Loading)
    val uiState: StateFlow<VagaDetailsUiState> = _uiState.asStateFlow()

    init {
        val vagaId: Int? = savedStateHandle["vagaId"]
        if (vagaId != null) {
            val vagaFlow = vagaRepository.getVagaById(vagaId)
            val candidaturasFlow = candidaturaRepository.getCandidaturas()

            combine(vagaFlow, candidaturasFlow) { vaga, candidaturas ->
                if (vaga == null) {
                    _uiState.value = VagaDetailsUiState.Error
                } else {
                    val isCandidatado = candidaturas.any { it.id == vaga.id }
                    _uiState.value = VagaDetailsUiState.Success(vaga, isCandidatado)
                }
            }.launchIn(viewModelScope)
        } else {
            _uiState.value = VagaDetailsUiState.Error
        }
    }

    fun applyToJob(vaga: Vaga) {
        viewModelScope.launch {
            candidaturaRepository.addCandidatura(vaga)
        }
    }
}
