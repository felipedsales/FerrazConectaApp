package com.example.ferrazconectaapp.di

import android.content.Context
import com.example.ferrazconectaapp.data.dao.CandidaturaDao
import com.example.ferrazconectaapp.data.dao.VagaDao
import com.example.ferrazconectaapp.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideVagaDao(database: AppDatabase): VagaDao {
        return database.vagaDao()
    }

    @Provides
    @Singleton
    fun provideCandidaturaDao(database: AppDatabase): CandidaturaDao {
        return database.candidaturaDao()
    }
}
