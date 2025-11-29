package com.example.ferrazconectaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ferrazconectaapp.MyApplication
import com.example.ferrazconectaapp.data.model.Vaga
import com.example.ferrazconectaapp.ui.components.VagaListItem
import com.example.ferrazconectaapp.ui.nav.Routes
import com.example.ferrazconectaapp.ui.theme.FerrazConectaAppTheme
import com.example.ferrazconectaapp.ui.viewmodels.FilterState
import com.example.ferrazconectaapp.ui.viewmodels.HomeUiState
import com.example.ferrazconectaapp.ui.viewmodels.HomeViewModel

@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {
    val uiState by homeViewModel.uiState.collectAsState()
    val searchQuery by homeViewModel.searchQuery.collectAsState()
    val filters by homeViewModel.filters.collectAsState()
    val isFilterSheetVisible by homeViewModel.isFilterSheetVisible.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val showSnackbar by navBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("show_snackbar")
        ?.observeAsState(initial = false) ?: remember { mutableStateOf(false) }

    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar("Candidatura realizada com sucesso!")
            navBackStackEntry?.savedStateHandle?.remove<Boolean>("show_snackbar")
        }
    }

    HomeScreenContent(
        navController = navController,
        uiState = uiState,
        searchQuery = searchQuery,
        onSearchQueryChange = homeViewModel::onSearchQueryChange,
        snackbarHostState = snackbarHostState,
        isFilterSheetVisible = isFilterSheetVisible,
        filters = filters,
        onApplyFilters = homeViewModel::onApplyFilters,
        onShowFilterSheet = homeViewModel::showFilterSheet,
        onHideFilterSheet = homeViewModel::hideFilterSheet
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    navController: NavController,
    uiState: HomeUiState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    isFilterSheetVisible: Boolean,
    filters: FilterState,
    onApplyFilters: (FilterState) -> Unit,
    onShowFilterSheet: () -> Unit,
    onHideFilterSheet: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Routes.HOME

    val items = listOf(
        Screen.Home, 
        Screen.Candidaturas, 
        Screen.Perfil
    )

    if (isFilterSheetVisible) {
        FilterBottomSheet(
            onDismiss = onHideFilterSheet,
            onApplyFilters = onApplyFilters,
            initialFilters = filters
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                placeholder = { Text("Buscar por cargo ou empresa...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                trailingIcon = {
                    IconButton(onClick = onShowFilterSheet) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtros")
                    }
                },
                singleLine = true
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = { 
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (uiState) {
                is HomeUiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Carregando vagas...")
                    }
                }
                is HomeUiState.Success -> {
                    val vagas = uiState.vagas
                    if (vagas.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(32.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Filled.CloudOff, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Nenhuma vaga encontrada",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tente uma busca ou filtros diferentes.",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(vagas) { vaga ->
                                VagaListItem(vaga = vaga, onClick = { navController.navigate(Routes.createVagaDetailsRoute(vaga.id)) })
                            }
                        }
                    }
                }
            }
        }
    }
}

// Sealed class para os itens da BottomBar
sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen(Routes.HOME, "Vagas", Icons.Default.Home)
    object Candidaturas : Screen(Routes.CANDIDATURAS, "Candidaturas", Icons.AutoMirrored.Filled.ListAlt)
    object Perfil : Screen(Routes.PERFIL, "Perfil", Icons.Default.Person)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit,
    onApplyFilters: (FilterState) -> Unit,
    initialFilters: FilterState
) {
    val sheetState = rememberModalBottomSheetState()
    var localizacao by remember { mutableStateOf(initialFilters.localizacao ?: "") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Filtros", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(24.dp))
            
            OutlinedTextField(
                value = localizacao,
                onValueChange = { localizacao = it },
                label = { Text("Localização") },
                modifier = Modifier.fillMaxWidth()
            )
            
            // TODO: Adicionar outros campos de filtro (nível de experiência, etc.)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { 
                    onApplyFilters(FilterState(localizacao = localizacao))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aplicar Filtros")
            }
        }
    }
}


@Preview(showBackground = true, name = "Estado com Vagas")
@Composable
fun HomeScreenSuccessPreview() {
    val vaga = Vaga(id = 1, titulo = "Desenvolvedor Android", empresa = "Google", descricao = "", local = "SP")
    FerrazConectaAppTheme {
        HomeScreenContent(
            navController = rememberNavController(),
            uiState = HomeUiState.Success(listOf(vaga, vaga, vaga)),
            searchQuery = "",
            onSearchQueryChange = {},
            snackbarHostState = remember { SnackbarHostState() },
            isFilterSheetVisible = false,
            filters = FilterState(),
            onApplyFilters = {},
            onShowFilterSheet = {},
            onHideFilterSheet = {}
        )
    }
}

@Preview(showBackground = true, name = "Estado Vazio")
@Composable
fun HomeScreenEmptyPreview() {
    FerrazConectaAppTheme {
        HomeScreenContent(
            navController = rememberNavController(),
            uiState = HomeUiState.Success(emptyList()),
            searchQuery = "",
            onSearchQueryChange = {},
            snackbarHostState = remember { SnackbarHostState() },
            isFilterSheetVisible = false,
            filters = FilterState(),
            onApplyFilters = {},
            onShowFilterSheet = {},
            onHideFilterSheet = {}
        )
    }
}

@Preview(showBackground = true, name = "Estado Carregando")
@Composable
fun HomeScreenLoadingPreview() {
    FerrazConectaAppTheme {
        HomeScreenContent(
            navController = rememberNavController(),
            uiState = HomeUiState.Loading,
            searchQuery = "",
            onSearchQueryChange = {},
            snackbarHostState = remember { SnackbarHostState() },
            isFilterSheetVisible = false,
            filters = FilterState(),
            onApplyFilters = {},
            onShowFilterSheet = {},
            onHideFilterSheet = {}
        )
    }
}

@Preview(showBackground = true, name = "BottomSheet de Filtro")
@Composable
fun FilterBottomSheetPreview() {
    FerrazConectaAppTheme {
        FilterBottomSheet(
            onDismiss = {},
            onApplyFilters = {},
            initialFilters = FilterState(localizacao = "Ferraz")
        )
    }
}
