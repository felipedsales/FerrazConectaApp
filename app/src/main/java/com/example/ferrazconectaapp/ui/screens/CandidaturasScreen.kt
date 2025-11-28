package com.example.ferrazconectaapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ferrazconectaapp.ui.components.VagaListItem
import com.example.ferrazconectaapp.ui.theme.FerrazConectaAppTheme
import com.example.ferrazconectaapp.ui.viewmodels.CandidaturasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidaturasScreen(navController: NavController, candidaturasViewModel: CandidaturasViewModel = viewModel()) {
    val candidaturas by candidaturasViewModel.candidaturas.collectAsState()

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
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(candidaturas) { vaga ->
                VagaListItem(vaga = vaga, onClick = { /* TODO: Navigate to VagaDetails */ })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CandidaturasScreenPreview() {
    FerrazConectaAppTheme {
        CandidaturasScreen(navController = rememberNavController())
    }
}
