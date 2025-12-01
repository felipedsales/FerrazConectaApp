package com.example.ferrazconectaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ferrazconectaapp.data.model.Vaga
import com.example.ferrazconectaapp.data.repository.VagaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

// Sealed interface para o estado da UI
sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val vagas: List<Vaga>) : HomeUiState
}

// Data class para o estado dos filtros
data class FilterState(
    val localizacao: String? = null,
    val nivelExperiencia: String? = null,
    val tipoContrato: String? = null,
    val areaAtuacao: String? = null
)

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val vagaRepository: VagaRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _filters = MutableStateFlow(FilterState())
    val filters = _filters.asStateFlow()

    // State para controlar a visibilidade do BottomSheet de filtros
    private val _isFilterSheetVisible = MutableStateFlow(false)
    val isFilterSheetVisible = _isFilterSheetVisible.asStateFlow()

    // Combina a busca (com debounce), os filtros e as vagas do repositório
    val uiState: StateFlow<HomeUiState> = combine(
        _searchQuery.debounce(500), // Debounce para a busca por texto
        _filters,
        vagaRepository.getVagas()
    ) { query, filters, vagas ->
        val filteredList = vagas.filter { vaga ->
            val matchesQuery = query.isBlank() ||
                               vaga.titulo.contains(query, ignoreCase = true) ||
                               vaga.empresa.contains(query, ignoreCase = true)

            val matchesLocation = filters.localizacao.isNullOrBlank() ||
                                  vaga.local.contains(filters.localizacao, ignoreCase = true)

            val matchesNivel = filters.nivelExperiencia.isNullOrBlank() ||
                               vaga.nivel.equals(filters.nivelExperiencia, ignoreCase = true)

            val matchesContrato = filters.tipoContrato.isNullOrBlank() ||
                                  vaga.contrato.equals(filters.tipoContrato, ignoreCase = true)

            val matchesArea = filters.areaAtuacao.isNullOrBlank() ||
                              vaga.area.equals(filters.areaAtuacao, ignoreCase = true)

            matchesQuery && matchesLocation && matchesNivel && matchesContrato && matchesArea
        }
        HomeUiState.Success(filteredList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onApplyFilters(newFilters: FilterState) {
        _filters.value = newFilters
        hideFilterSheet()
    }

    fun showFilterSheet() {
        _isFilterSheetVisible.value = true
    }

    fun hideFilterSheet() {
        _isFilterSheetVisible.value = false
    }
}
