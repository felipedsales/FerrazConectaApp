package com.example.ferrazconectaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VagaDetailsViewModel : ViewModel() {

    private val _showApplicationSuccess = MutableStateFlow(false)
    val showApplicationSuccess: StateFlow<Boolean> = _showApplicationSuccess

    fun applyToJob(vagaId: Int) {
        // TODO: Implementar a lógica real de candidatura (ex: chamada de API)
        println("Candidatura para a vaga $vagaId realizada com sucesso.")
        _showApplicationSuccess.value = true
    }

    fun onSuccessMessageShown() {
        _showApplicationSuccess.value = false
    }
}
