package com.example.ferrazconectaapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ferrazconectaapp.data.model.Vaga
import com.example.ferrazconectaapp.ui.theme.FerrazConectaAppTheme
import com.example.ferrazconectaapp.ui.viewmodels.VagaDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VagaDetailsScreen(
    navController: NavController, 
    vaga: Vaga, 
    vagaDetailsViewModel: VagaDetailsViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val showApplicationSuccess by vagaDetailsViewModel.showApplicationSuccess.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(vaga.titulo) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = vaga.titulo, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = vaga.empresa, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = vaga.local, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Descrição da Vaga", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = vaga.descricao, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { vagaDetailsViewModel.applyToJob(vaga.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Candidatar-se")
            }
        }
    }

    if (showApplicationSuccess) {
        LaunchedEffect(Unit) {
            snackbarHostState.showSnackbar("Candidatura realizada com sucesso!")
            vagaDetailsViewModel.onSuccessMessageShown()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VagaDetailsScreenPreview() {
    val vaga = Vaga(
        id = 1,
        titulo = "Desenvolvedor Android Sênior",
        empresa = "Google",
        descricao = "Estamos procurando um desenvolvedor Android incrível para se juntar à nossa equipe. Você trabalhará em projetos desafiadores e inovadores, usando as tecnologias mais recentes. Requisitos: 5 anos de experiência com desenvolvimento Android (Kotlin), conhecimento em Jetpack Compose, arquitetura MVVM e testes unitários.",
        local = "São Paulo, SP"
    )
    FerrazConectaAppTheme {
        VagaDetailsScreen(navController = rememberNavController(), vaga = vaga)
    }
}
