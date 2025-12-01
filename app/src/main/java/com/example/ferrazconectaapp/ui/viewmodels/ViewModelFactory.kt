package com.example.ferrazconectaapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.ferrazconectaapp.data.repository.AuthRepository
import com.example.ferrazconectaapp.data.repository.CandidaturaRepository
import com.example.ferrazconectaapp.data.repository.UserRepository
import com.example.ferrazconectaapp.data.repository.VagaRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ViewModelFactoryEntryPoint {
    fun vagaRepository(): VagaRepository
    fun candidaturaRepository(): CandidaturaRepository
    fun authRepository(): AuthRepository
    fun userRepository(): UserRepository
}

object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

            val entryPoint = EntryPointAccessors.fromApplication(
                application.applicationContext,
                ViewModelFactoryEntryPoint::class.java
            )

            return when {
                modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                    HomeViewModel(entryPoint.vagaRepository()) as T
                }
                modelClass.isAssignableFrom(CandidaturasViewModel::class.java) -> {
                    CandidaturasViewModel(entryPoint.candidaturaRepository()) as T
                }
                modelClass.isAssignableFrom(VagaDetailsViewModel::class.java) -> {
                    VagaDetailsViewModel(
                        vagaRepository = entryPoint.vagaRepository(),
                        candidaturaRepository = entryPoint.candidaturaRepository(),
                        savedStateHandle = extras.createSavedStateHandle()
                    ) as T
                }
                modelClass.isAssignableFrom(CadastroViewModel::class.java) -> {
                    CadastroViewModel(entryPoint.authRepository()) as T
                }
                modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                    ProfileViewModel(entryPoint.authRepository(), entryPoint.userRepository()) as T
                }
                modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                    LoginViewModel(entryPoint.authRepository()) as T
                }
                modelClass.isAssignableFrom(FormEscolaridadeViewModel::class.java) -> {
                    FormEscolaridadeViewModel(
                        userRepository = entryPoint.userRepository(),
                        savedStateHandle = extras.createSavedStateHandle()
                    ) as T
                }
                modelClass.isAssignableFrom(FormExperienciaViewModel::class.java) -> {
                    FormExperienciaViewModel(
                        userRepository = entryPoint.userRepository(),
                        savedStateHandle = extras.createSavedStateHandle()
                    ) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
