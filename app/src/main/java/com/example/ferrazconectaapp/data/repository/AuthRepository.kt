package com.example.ferrazconectaapp.data.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

/**
 * Repositório para lidar com a autenticação.
 */
@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    suspend fun register(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass).await()
    }

    suspend fun login(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass).await()
    }

    suspend fun firebaseSignInWithGoogle(credential: AuthCredential) {
        auth.signInWithCredential(credential).await()
    }
}
