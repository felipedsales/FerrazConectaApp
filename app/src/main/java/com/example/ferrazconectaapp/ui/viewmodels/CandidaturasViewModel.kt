package com.example.ferrazconectaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ferrazconectaapp.data.model.Candidatura
import com.example.ferrazconectaapp.data.model.VagaComStatus
import com.example.ferrazconectaapp.data.repository.CandidaturaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface CandidaturasUiState {
    object Loading : CandidaturasUiState
    data class Success(val vagasComStatus: List<VagaComStatus>) : CandidaturasUiState
    object Empty : CandidaturasUiState
}

sealed interface CandidaturaEvent {
    data class DesistenciaSuccess(val message: String) : CandidaturaEvent
}

@HiltViewModel
class CandidaturasViewModel @Inject constructor(
    private val candidaturaRepository: CandidaturaRepository
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<CandidaturaEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

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

    fun desistirCandidatura(vagaComStatus: VagaComStatus) {
        viewModelScope.launch {
            val candidaturaParaDeletar = Candidatura(
                id = vagaComStatus.candidaturaId,
                vagaId = vagaComStatus.vaga.id,
                status = vagaComStatus.status
            )
            candidaturaRepository.deleteCandidatura(candidaturaParaDeletar)
            _eventFlow.emit(CandidaturaEvent.DesistenciaSuccess("Você desistiu da vaga: ${vagaComStatus.vaga.titulo}"))
        }
    }
}
