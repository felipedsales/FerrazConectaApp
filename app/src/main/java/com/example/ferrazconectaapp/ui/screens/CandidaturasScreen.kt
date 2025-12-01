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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ferrazconectaapp.data.model.Vaga
import com.example.ferrazconectaapp.data.model.VagaComStatus
import com.example.ferrazconectaapp.ui.components.VagaListItem
import com.example.ferrazconectaapp.ui.nav.Routes
import com.example.ferrazconectaapp.ui.theme.FerrazConectaAppTheme
import com.example.ferrazconectaapp.ui.viewmodels.CandidaturaEvent
import com.example.ferrazconectaapp.ui.viewmodels.CandidaturasUiState
import com.example.ferrazconectaapp.ui.viewmodels.CandidaturasViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CandidaturasScreen(navController: NavController, candidaturasViewModel: CandidaturasViewModel = hiltViewModel()) {
    val uiState by candidaturasViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        candidaturasViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CandidaturaEvent.DesistenciaSuccess -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    CandidaturasScreenContent(
        navController = navController,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onDesistirClick = candidaturasViewModel::desistirCandidatura
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidaturasScreenContent(
    navController: NavController,
    uiState: CandidaturasUiState,
    snackbarHostState: SnackbarHostState,
    onDesistirClick: (VagaComStatus) -> Unit
) {
    val (vagaParaDesistir, setVagaParaDesistir) = remember { mutableStateOf<VagaComStatus?>(null) }

    vagaParaDesistir?.let { vaga ->
        AlertDialog(
            onDismissRequest = { setVagaParaDesistir(null) },
            title = { Text("Confirmar Desistência") },
            text = { Text("Você tem certeza que deseja desistir da vaga de ${vaga.vaga.titulo}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDesistirClick(vaga)
                        setVagaParaDesistir(null)
                    }
                ) {
                    Text("Sim, desistir")
                }
            },
            dismissButton = {
                TextButton(onClick = { setVagaParaDesistir(null) }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                        items(uiState.vagasComStatus) { vagaComStatus ->
                            VagaListItem(
                                vaga = vagaComStatus.vaga,
                                onClick = { navController.navigate(Routes.createVagaDetailsRoute(vagaComStatus.vaga.id)) },
                                status = vagaComStatus.status,
                                onDesistirClick = { setVagaParaDesistir(vagaComStatus) }
                            )
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
    val vagaComStatus = VagaComStatus(vaga = vaga, status = "Enviada", candidaturaId = 1)
    FerrazConectaAppTheme {
        CandidaturasScreenContent(
            navController = rememberNavController(),
            uiState = CandidaturasUiState.Success(listOf(vagaComStatus, vagaComStatus)),
            snackbarHostState = remember { SnackbarHostState() },
            onDesistirClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Estado Vazio")
@Composable
fun CandidaturasScreenEmptyPreview() {
    FerrazConectaAppTheme {
        CandidaturasScreenContent(
            navController = rememberNavController(),
            uiState = CandidaturasUiState.Empty,
            snackbarHostState = remember { SnackbarHostState() },
            onDesistirClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Estado Carregando")
@Composable
fun CandidaturasScreenLoadingPreview() {
    FerrazConectaAppTheme {
        CandidaturasScreenContent(
            navController = rememberNavController(),
            uiState = CandidaturasUiState.Loading,
            snackbarHostState = remember { SnackbarHostState() },
            onDesistirClick = {}
        )
    }
}
