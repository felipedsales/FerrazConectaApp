package com.example.ferrazconectaapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ferrazconectaapp.ui.components.VagaListItem
import com.example.ferrazconectaapp.ui.theme.FerrazConectaAppTheme
import com.example.ferrazconectaapp.ui.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    val vagas by homeViewModel.vagas.collectAsState()
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Vagas", "Candidaturas", "Perfil")
    val icons = listOf(Icons.Filled.Home, Icons.AutoMirrored.Filled.ListAlt, Icons.Filled.Person)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Vagas Disponíveis") }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(vagas) { vaga ->
                VagaListItem(vaga = vaga, onClick = { /* FAZER: Navegar para detalhes da vaga */ })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FerrazConectaAppTheme {
        HomeScreen()
    }
}
