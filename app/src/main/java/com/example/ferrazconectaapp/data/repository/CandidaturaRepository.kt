package com.example.ferrazconectaapp.data.repository

import com.example.ferrazconectaapp.data.dao.CandidaturaDao
import com.example.ferrazconectaapp.data.model.Candidatura
import com.example.ferrazconectaapp.data.model.Vaga
import com.example.ferrazconectaapp.data.model.VagaComStatus
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class CandidaturaRepository @Inject constructor(private val candidaturaDao: CandidaturaDao) {

    fun getCandidaturas(): Flow<List<VagaComStatus>> = candidaturaDao.getCandidaturasComStatus()

    suspend fun addCandidatura(vaga: Vaga) {
        candidaturaDao.insert(Candidatura(vagaId = vaga.id))
    }

    suspend fun deleteCandidatura(candidatura: Candidatura) {
        candidaturaDao.delete(candidatura)
    }
}
