package com.example.ferrazconectaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ferrazconectaapp.data.model.Vaga
import com.example.ferrazconectaapp.ui.theme.FerrazConectaAppTheme
import com.example.ferrazconectaapp.ui.viewmodels.VagaDetailsUiState
import com.example.ferrazconectaapp.ui.viewmodels.VagaDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VagaDetailsScreen(navController: NavController, vagaDetailsViewModel: VagaDetailsViewModel = hiltViewModel()) {
    val uiState by vagaDetailsViewModel.uiState.collectAsState()

    when (val state = uiState) {
        is VagaDetailsUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is VagaDetailsUiState.Success -> {
            VagaDetailsContent(
                navController = navController,
                vaga = state.vaga,
                isCandidatado = state.isCandidatado,
                onApplyClick = {
                    vagaDetailsViewModel.applyToJob(state.vaga)
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("show_snackbar", true)
                    navController.popBackStack()
                }
            )
        }
        is VagaDetailsUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Default.Error, contentDescription = null)
                Text("Erro ao carregar a vaga.")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VagaDetailsContent(
    navController: NavController,
    vaga: Vaga,
    isCandidatado: Boolean,
    onApplyClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(vaga.titulo) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                scrollBehavior = scrollBehavior
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
            Text(text = vaga.titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Business, contentDescription = "Empresa", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text(text = vaga.empresa, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Local", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text(text = vaga.local, style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Sobre a vaga", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = vaga.descricao, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.weight(1f))

            if (isCandidatado) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Text("Você já se candidatou a esta vaga.", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                Button(
                    onClick = onApplyClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Candidatar-se")
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Não Candidatado")
@Composable
fun VagaDetailsScreenNotAppliedPreview() {
    val vaga = Vaga(
        id = 1,
        titulo = "Desenvolvedor Android Sênior",
        empresa = "Google",
        descricao = "Descrição da vaga.",
        local = "São Paulo, SP"
    )
    FerrazConectaAppTheme {
        VagaDetailsContent(
            navController = rememberNavController(),
            vaga = vaga,
            isCandidatado = false,
            onApplyClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Já Candidatado")
@Composable
fun VagaDetailsScreenAppliedPreview() {
    val vaga = Vaga(
        id = 1,
        titulo = "Desenvolvedor Android Sênior",
        empresa = "Google",
        descricao = "Descrição da vaga.",
        local = "São Paulo, SP"
    )
    FerrazConectaAppTheme {
        VagaDetailsContent(
            navController = rememberNavController(),
            vaga = vaga,
            isCandidatado = true,
            onApplyClick = {}
        )
    }
}
