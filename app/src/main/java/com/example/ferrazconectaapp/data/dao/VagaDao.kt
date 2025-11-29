package com.example.ferrazconectaapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ferrazconectaapp.data.model.Vaga
import kotlinx.coroutines.flow.Flow

@Dao
interface VagaDao {

    @Query("SELECT * FROM vagas")
    fun getVagas(): Flow<List<Vaga>>

    @Query("SELECT * FROM vagas WHERE id = :id")
    fun getVagaById(id: Int): Flow<Vaga?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vagas: List<Vaga>)
}
