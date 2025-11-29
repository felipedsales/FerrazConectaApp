package com.example.ferrazconectaapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ferrazconectaapp.data.model.Candidatura
import com.example.ferrazconectaapp.data.model.Vaga
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidaturaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(candidatura: Candidatura)

    @Query("SELECT v.* FROM vagas v INNER JOIN candidaturas c ON v.id = c.vagaId")
    fun getCandidaturas(): Flow<List<Vaga>>
}
