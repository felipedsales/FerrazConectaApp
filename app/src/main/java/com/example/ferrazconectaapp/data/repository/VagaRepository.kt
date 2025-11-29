package com.example.ferrazconectaapp.data.repository

import com.example.ferrazconectaapp.data.dao.VagaDao
import com.example.ferrazconectaapp.data.model.Vaga
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class VagaRepository @Inject constructor(private val vagaDao: VagaDao) {

    fun getVagas(): Flow<List<Vaga>> = vagaDao.getVagas()

    fun getVagaById(id: Int): Flow<Vaga?> = vagaDao.getVagaById(id)
}
