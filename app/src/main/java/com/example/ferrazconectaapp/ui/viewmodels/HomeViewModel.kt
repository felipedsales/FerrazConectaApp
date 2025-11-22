package com.example.ferrazconectaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.ferrazconectaapp.data.model.Vaga
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val _vagas = MutableStateFlow<List<Vaga>>(emptyList())
    val vagas = _vagas.asStateFlow()

    init {
        // Dados de exemplo
        _vagas.value = listOf(
            Vaga(1, "Desenvolvedor Android Pleno", "Secretaria de Tecnologia", "Desenvolvimento e manutenção de aplicativos para a prefeitura.", "Ferraz de Vasconcelos, SP"),
            Vaga(2, "Analista de Sistemas", "Secretaria de Planejamento", "Análise e levantamento de requisitos para novos sistemas.", "Ferraz de Vasconcelos, SP"),
            Vaga(3, "Agente Administrativo", "Secretaria de Administração", "Rotinas administrativas, atendimento ao público e organização de documentos.", "Ferraz de Vasconcelos, SP"),
            Vaga(4, "Professor de Educação Infantil", "Secretaria de Educação", "Lecionar para turmas de educação infantil, cuidando do desenvolvimento e bem-estar das crianças.", "Ferraz de Vasconcelos, SP"),
            Vaga(5, "Técnico de Enfermagem", "Secretaria de Saúde", "Prestar assistência de enfermagem aos pacientes na rede municipal de saúde.", "Ferraz de Vasconcelos, SP")
        )
    }
}
