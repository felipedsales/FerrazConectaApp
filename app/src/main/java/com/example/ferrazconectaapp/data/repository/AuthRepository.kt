package com.example.ferrazconectaapp.data.repository

import com.example.ferrazconectaapp.data.model.User
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

/**
 * Repositório para lidar com a autenticação.
 */
@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun register(email: String, pass: String): String {
        val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
        return authResult.user?.uid ?: throw Exception("UID do usuário não encontrado")
    }

    suspend fun login(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass).await()
    }

    suspend fun firebaseSignInWithGoogle(credential: AuthCredential) {
        auth.signInWithCredential(credential).await()
    }

    suspend fun createUser(user: User) {
        firestore.collection("users").document(user.uid).set(user).await()
    }

    suspend fun getUser(uid: String): User? {
        return firestore.collection("users").document(uid).get().await().toObject(User::class.java)
    }
}
