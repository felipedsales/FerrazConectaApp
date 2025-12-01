package com.example.ferrazconectaapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "candidaturas",
    foreignKeys = [
        ForeignKey(
            entity = Vaga::class,
            parentColumns = ["id"],
            childColumns = ["vagaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["vagaId"])]
)
data class Candidatura(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val vagaId: Int,
    val status: String = "Enviada"
)
