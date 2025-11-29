package com.example.ferrazconectaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ferrazconectaapp.data.model.Vaga
import com.example.ferrazconectaapp.data.repository.CandidaturaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface CandidaturasUiState {
    object Loading : CandidaturasUiState
    data class Success(val vagas: List<Vaga>) : CandidaturasUiState
    object Empty : CandidaturasUiState
}

@HiltViewModel
class CandidaturasViewModel @Inject constructor(
    candidaturaRepository: CandidaturaRepository
) : ViewModel() {

    val uiState: StateFlow<CandidaturasUiState> = candidaturaRepository.getCandidaturas()
        .map { candidaturas ->
            if (candidaturas.isEmpty()) {
                CandidaturasUiState.Empty
            } else {
                CandidaturasUiState.Success(candidaturas)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CandidaturasUiState.Loading
        )
}
