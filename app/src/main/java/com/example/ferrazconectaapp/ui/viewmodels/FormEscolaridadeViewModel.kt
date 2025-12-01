package com.example.ferrazconectaapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ferrazconectaapp.data.model.Escolaridade
import com.example.ferrazconectaapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

sealed class FormResult {
    object SalvoComSucesso : FormResult()
    data class Erro(val mensagem: String) : FormResult()
}

@HiltViewModel
class FormEscolaridadeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var instituicao by mutableStateOf("")
        private set
    var curso by mutableStateOf("")
        private set
    var nivel by mutableStateOf("")
        private set
    var dataInicio by mutableStateOf("")
        private set
    var dataFim by mutableStateOf("")
        private set

    private var escolaridadeId: String? = null

    private val _formResult = MutableSharedFlow<FormResult>()
    val formResult = _formResult.asSharedFlow()

    init {
        escolaridadeId = savedStateHandle.get<String>("escolaridadeId")
        if (escolaridadeId != null) {
            loadEscolaridade()
        }
    }

    private fun loadEscolaridade() {
        viewModelScope.launch {
            val user = userRepository.getUser()
            val escolaridade = user?.escolaridade?.find { it.id == escolaridadeId }
            if (escolaridade != null) {
                instituicao = escolaridade.instituicao
                curso = escolaridade.curso
                nivel = escolaridade.nivel
                dataInicio = escolaridade.dataInicio
                dataFim = escolaridade.dataFim
            }
        }
    }

    fun onInstituicaoChange(novoValor: String) {
        instituicao = novoValor
    }

    fun onCursoChange(novoValor: String) {
        curso = novoValor
    }

    fun onNivelChange(novoValor: String) {
        nivel = novoValor
    }

    fun onDataInicioChange(novoValor: String) {
        dataInicio = novoValor
    }

    fun onDataFimChange(novoValor: String) {
        dataFim = novoValor
    }

    fun salvarEscolaridade() {
        viewModelScope.launch {
            if (instituicao.isBlank() || curso.isBlank() || nivel.isBlank() || dataInicio.isBlank() || dataFim.isBlank()) {
                _formResult.emit(FormResult.Erro("Preencha todos os campos."))
                return@launch
            }

            val escolaridade = Escolaridade(
                id = escolaridadeId ?: UUID.randomUUID().toString(),
                instituicao = instituicao,
                curso = curso,
                nivel = nivel,
                dataInicio = dataInicio,
                dataFim = dataFim
            )

            try {
                if (escolaridadeId == null) {
                    userRepository.addEscolaridade(escolaridade)
                } else {
                    userRepository.updateEscolaridade(escolaridade)
                }
                _formResult.emit(FormResult.SalvoComSucesso)
            } catch (e: Exception) {
                _formResult.emit(FormResult.Erro(e.message ?: "Falha ao salvar."))
            }
        }
    }
}
