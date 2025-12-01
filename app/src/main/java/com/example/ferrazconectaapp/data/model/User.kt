package com.example.ferrazconectaapp.data.model

data class User(
    val uid: String = "",
    val nome: String = "",
    val email: String = "",
    val cpf: String = "",
    val dataNascimento: String = "",
    val telefone: String = "",
    val endereco: String = "",
    val resumoProfissional: String = "",
    val escolaridade: List<Escolaridade> = emptyList(),
    val experienciaProfissional: List<ExperienciaProfissional> = emptyList()
)
