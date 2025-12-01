package com.example.ferrazconectaapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ferrazconectaapp.data.model.Candidatura
import com.example.ferrazconectaapp.data.model.VagaComStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidaturaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(candidatura: Candidatura)

    @Delete
    suspend fun delete(candidatura: Candidatura)

    @Query("SELECT v.*, c.status, c.id as candidaturaId FROM vagas v INNER JOIN candidaturas c ON v.id = c.vagaId")
    fun getCandidaturasComStatus(): Flow<List<VagaComStatus>>
}
