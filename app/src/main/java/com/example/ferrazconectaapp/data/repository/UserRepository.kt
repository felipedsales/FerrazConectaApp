package com.example.ferrazconectaapp.data.repository

import com.example.ferrazconectaapp.data.model.Escolaridade
import com.example.ferrazconectaapp.data.model.ExperienciaProfissional
import com.example.ferrazconectaapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    private fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: throw IllegalStateException("Usuário não está logado.")
    }

    suspend fun getUser(): User? {
        val userId = getCurrentUserId()
        val document = firestore.collection("users").document(userId).get().await()
        return document.toObject(User::class.java)
    }

    suspend fun addEscolaridade(escolaridade: Escolaridade) {
        val userId = getCurrentUserId()
        firestore.collection("users").document(userId).update(
            "escolaridade", FieldValue.arrayUnion(escolaridade)
        ).await()
    }

    suspend fun updateEscolaridade(escolaridade: Escolaridade) {
        val userId = getCurrentUserId()
        val user = getUser() ?: return
        val updatedList = user.escolaridade.map { if (it.id == escolaridade.id) escolaridade else it }
        firestore.collection("users").document(userId).update("escolaridade", updatedList).await()
    }

    suspend fun deleteEscolaridade(escolaridade: Escolaridade) {
        val userId = getCurrentUserId()
        firestore.collection("users").document(userId).update(
            "escolaridade", FieldValue.arrayRemove(escolaridade)
        ).await()
    }

    suspend fun addExperienciaProfissional(experiencia: ExperienciaProfissional) {
        val userId = getCurrentUserId()
        firestore.collection("users").document(userId).update(
            "experienciaProfissional", FieldValue.arrayUnion(experiencia)
        ).await()
    }

    suspend fun updateExperienciaProfissional(experiencia: ExperienciaProfissional) {
        val userId = getCurrentUserId()
        val user = getUser() ?: return
        val updatedList = user.experienciaProfissional.map { if (it.id == experiencia.id) experiencia else it }
        firestore.collection("users").document(userId).update("experienciaProfissional", updatedList).await()
    }

    suspend fun deleteExperienciaProfissional(experiencia: ExperienciaProfissional) {
        val userId = getCurrentUserId()
        firestore.collection("users").document(userId).update(
            "experienciaProfissional", FieldValue.arrayRemove(experiencia)
        ).await()
    }
}
