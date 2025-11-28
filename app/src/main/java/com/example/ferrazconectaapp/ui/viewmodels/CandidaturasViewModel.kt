package com.example.ferrazconectaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.ferrazconectaapp.data.model.Vaga
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CandidaturasViewModel : ViewModel() {
    private val _candidaturas = MutableStateFlow<List<Vaga>>(emptyList())
    val candidaturas: StateFlow<List<Vaga>> = _candidaturas

    init {
        // TODO: Carregar as vagas para as quais o usuário se candidatou
    }
}
