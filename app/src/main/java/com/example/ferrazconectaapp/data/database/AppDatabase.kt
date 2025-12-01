package com.example.ferrazconectaapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ferrazconectaapp.data.dao.CandidaturaDao
import com.example.ferrazconectaapp.data.dao.VagaDao
import com.example.ferrazconectaapp.data.model.Candidatura
import com.example.ferrazconectaapp.data.model.Vaga
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Vaga::class, Candidatura::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun vagaDao(): VagaDao
    abstract fun candidaturaDao(): CandidaturaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ferraz_conecta_database"
                )
                .fallbackToDestructiveMigration() // Adicionado para evitar crashes em migrações
                .addCallback(AppDatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    // Pré-popula o banco de dados com vagas iniciais
                    val vagaDao = it.vagaDao()
                    vagaDao.insertAll(getInitialVagas())
                }
            }
        }

        private fun getInitialVagas(): List<Vaga> {
            return listOf(
                Vaga(titulo = "Desenvolvedor Android Pleno", empresa = "Secretaria de Tecnologia", descricao = "Desenvolvimento e manutenção de aplicativos para a prefeitura.", local = "Ferraz de Vasconcelos, SP", nivel = "Com Experiência", contrato = "PJ", area = "Tecnologia"),
                Vaga(titulo = "Analista de Sistemas", empresa = "Secretaria de Planejamento", descricao = "Análise e levantamento de requisitos para novos sistemas.", local = "Ferraz de Vasconcelos, SP", nivel = "Com Experiência", contrato = "CLT", area = "Tecnologia"),
                Vaga(titulo = "Agente Administrativo", empresa = "Secretaria de Administração", descricao = "Rotinas administrativas, atendimento ao público e organização de documentos.", local = "Ferraz de Vasconcelos, SP", nivel = "Sem Experiência", contrato = "CLT", area = "Administrativo"),
                Vaga(titulo = "Professor de Educação Infantil", empresa = "Secretaria de Educação", descricao = "Lecionar para turmas de educação infantil, cuidando do desenvolvimento e bem-estar das crianças.", local = "Ferraz de Vasconcelos, SP", nivel = "Com Experiência", contrato = "CLT", area = "Educação"),
                Vaga(titulo = "Técnico de Enfermagem", empresa = "Secretaria de Saúde", descricao = "Prestar assistência de enfermagem aos pacientes na rede municipal de saúde.", local = "Ferraz de Vasconcelos, SP", nivel = "Sem Experiência", contrato = "Temporário", area = "Saúde"),
                Vaga(titulo = "Auxiliar de Serviços Gerais", empresa = "Secretaria de Obras", descricao = "Limpeza e manutenção das instalações da prefeitura.", local = "Ferraz de Vasconcelos, SP", nivel = "Sem Experiência", contrato = "CLT", area = "Serviços Gerais")
            )
        }
    }
}
