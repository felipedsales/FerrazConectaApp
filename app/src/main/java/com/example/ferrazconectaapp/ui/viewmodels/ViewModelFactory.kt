package com.example.ferrazconectaapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.ferrazconectaapp.MyApplication
import com.example.ferrazconectaapp.data.repository.AuthRepository
import com.example.ferrazconectaapp.data.repository.CandidaturaRepository
import com.example.ferrazconectaapp.data.repository.VagaRepository

object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as MyApplication

            return when {
                modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                    HomeViewModel(application.vagaRepository) as T
                }
                modelClass.isAssignableFrom(CandidaturasViewModel::class.java) -> {
                    CandidaturasViewModel(application.candidaturaRepository) as T
                }
                modelClass.isAssignableFrom(VagaDetailsViewModel::class.java) -> {
                    VagaDetailsViewModel(
                        vagaRepository = application.vagaRepository,
                        candidaturaRepository = application.candidaturaRepository,
                        savedStateHandle = extras.createSavedStateHandle()
                    ) as T
                }
                modelClass.isAssignableFrom(CadastroViewModel::class.java) -> {
                    CadastroViewModel(application.authRepository) as T
                }
                modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                    ProfileViewModel() as T
                }
                modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                    LoginViewModel() as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
