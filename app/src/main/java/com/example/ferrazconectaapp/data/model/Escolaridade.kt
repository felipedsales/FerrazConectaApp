package com.example.ferrazconectaapp.data.model

import java.util.UUID

data class Escolaridade(
    val id: String = UUID.randomUUID().toString(),
    val instituicao: String = "",
    val curso: String = "",
    val nivel: String = "",
    val dataInicio: String = "",
    val dataFim: String = ""
)
