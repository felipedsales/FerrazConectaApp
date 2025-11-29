package com.example.ferrazconectaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.WorkOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ferrazconectaapp.data.model.Vaga
import com.example.ferrazconectaapp.ui.components.VagaListItem
import com.example.ferrazconectaapp.ui.nav.Routes
import com.example.ferrazconectaapp.ui.theme.FerrazConectaAppTheme
import com.example.ferrazconectaapp.ui.viewmodels.CandidaturasUiState
import com.example.ferrazconectaapp.ui.viewmodels.CandidaturasViewModel

@Composable
fun CandidaturasScreen(navController: NavController, candidaturasViewModel: CandidaturasViewModel = hiltViewModel()) {
    val uiState by candidaturasViewModel.uiState.collectAsState()

    CandidaturasScreenContent(
        navController = navController,
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidaturasScreenContent(
    navController: NavController,
    uiState: CandidaturasUiState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Candidaturas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (uiState) {
                is CandidaturasUiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Carregando candidaturas...")
                    }
                }
                is CandidaturasUiState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(uiState.vagas) { vaga ->
                            VagaListItem(vaga = vaga, onClick = { navController.navigate(Routes.createVagaDetailsRoute(vaga.id)) })
                        }
                    }
                }
                is CandidaturasUiState.Empty -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.WorkOff, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Você ainda não se candidatou a nenhuma vaga",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "As vagas para as quais você se candidatar aparecerão aqui.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Estado com Vagas")
@Composable
fun CandidaturasScreenSuccessPreview() {
    val vaga = Vaga(
        id = 1,
        titulo = "Desenvolvedor Android Sênior",
        empresa = "Google",
        descricao = "Descrição da vaga.",
        local = "São Paulo, SP"
    )
    FerrazConectaAppTheme {
        CandidaturasScreenContent(
            navController = rememberNavController(),
            uiState = CandidaturasUiState.Success(listOf(vaga, vaga, vaga))
        )
    }
}

@Preview(showBackground = true, name = "Estado Vazio")
@Composable
fun CandidaturasScreenEmptyPreview() {
    FerrazConectaAppTheme {
        CandidaturasScreenContent(
            navController = rememberNavController(),
            uiState = CandidaturasUiState.Empty
        )
    }
}

@Preview(showBackground = true, name = "Estado Carregando")
@Composable
fun CandidaturasScreenLoadingPreview() {
    FerrazConectaAppTheme {
        CandidaturasScreenContent(
            navController = rememberNavController(),
            uiState = CandidaturasUiState.Loading
        )
    }
}
