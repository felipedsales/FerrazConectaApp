package com.example.ferrazconectaapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ferrazconectaapp.data.model.ExperienciaProfissional
import com.example.ferrazconectaapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class FormExperienciaViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var empresa by mutableStateOf("")
        private set
    var cargo by mutableStateOf("")
        private set
    var dataInicio by mutableStateOf("")
        private set
    var dataFim by mutableStateOf("")
        private set
    var descricao by mutableStateOf("")
        private set

    private var experienciaId: String? = null

    private val _formResult = MutableSharedFlow<FormResult>()
    val formResult = _formResult.asSharedFlow()

    init {
        experienciaId = savedStateHandle.get<String>("experienciaId")
        if (experienciaId != null) {
            loadExperiencia()
        }
    }

    private fun loadExperiencia() {
        viewModelScope.launch {
            val user = userRepository.getUser()
            val experiencia = user?.experienciaProfissional?.find { it.id == experienciaId }
            if (experiencia != null) {
                empresa = experiencia.empresa
                cargo = experiencia.cargo
                dataInicio = experiencia.dataInicio
                dataFim = experiencia.dataFim
                descricao = experiencia.descricao
            }
        }
    }

    fun onEmpresaChange(novoValor: String) {
        empresa = novoValor
    }

    fun onCargoChange(novoValor: String) {
        cargo = novoValor
    }

    fun onDataInicioChange(novoValor: String) {
        dataInicio = novoValor
    }

    fun onDataFimChange(novoValor: String) {
        dataFim = novoValor
    }

    fun onDescricaoChange(novoValor: String) {
        descricao = novoValor
    }

    fun salvarExperiencia() {
        viewModelScope.launch {
            if (empresa.isBlank() || cargo.isBlank() || dataInicio.isBlank() || dataFim.isBlank()) {
                _formResult.emit(FormResult.Erro("Preencha todos os campos obrigatórios."))
                return@launch
            }

            val experiencia = ExperienciaProfissional(
                id = experienciaId ?: UUID.randomUUID().toString(),
                empresa = empresa,
                cargo = cargo,
                dataInicio = dataInicio,
                dataFim = dataFim,
                descricao = descricao
            )

            try {
                if (experienciaId == null) {
                    userRepository.addExperienciaProfissional(experiencia)
                } else {
                    userRepository.updateExperienciaProfissional(experiencia)
                }
                _formResult.emit(FormResult.SalvoComSucesso)
            } catch (e: Exception) {
                _formResult.emit(FormResult.Erro(e.message ?: "Falha ao salvar."))
            }
        }
    }
}
