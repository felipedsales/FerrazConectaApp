package com.example.ferrazconectaapp.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay

/**
 * Repositório para lidar com a autenticação.
 * Em um app real, isso se comunicaria com uma API ou Firebase Auth.
 */
@Singleton
class AuthRepository @Inject constructor() {

    suspend fun register(email: String, pass: String) {
        // Simula uma chamada de rede
        delay(1000)
        println("Usuário registrado com sucesso: $email")
    }
}
