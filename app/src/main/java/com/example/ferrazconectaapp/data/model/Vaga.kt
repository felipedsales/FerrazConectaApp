package com.example.ferrazconectaapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vagas")
data class Vaga(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val empresa: String,
    val descricao: String,
    val local: String
)
