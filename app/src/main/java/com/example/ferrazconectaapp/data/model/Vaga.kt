package com.example.ferrazconectaapp.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vagas")
data class Vaga(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val empresa: String,
    val descricao: String,
    val local: String,
    val nivel: String = "",
    val contrato: String = "",
    val area: String = ""
)

// Classe de dados para combinar os resultados da consulta JOIN
data class VagaComStatus(
    @Embedded
    val vaga: Vaga,
    val status: String,
    val candidaturaId: Int
)
