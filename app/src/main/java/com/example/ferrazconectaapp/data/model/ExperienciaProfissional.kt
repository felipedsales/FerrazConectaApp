package com.example.ferrazconectaapp.data.model

import java.util.UUID

data class ExperienciaProfissional(
    val id: String = UUID.randomUUID().toString(),
    val empresa: String = "",
    val cargo: String = "",
    val dataInicio: String = "",
    val dataFim: String = "",
    val descricao: String = ""
)
