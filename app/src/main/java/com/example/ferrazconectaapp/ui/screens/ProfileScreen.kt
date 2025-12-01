package com.example.ferrazconectaapp.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.ferrazconectaapp.data.model.Escolaridade
import com.example.ferrazconectaapp.data.model.ExperienciaProfissional
import com.example.ferrazconectaapp.data.model.User
import com.example.ferrazconectaapp.ui.nav.Routes
import com.example.ferrazconectaapp.ui.theme.FerrazConectaAppTheme
import com.example.ferrazconectaapp.ui.viewmodels.ProfileUiState
import com.example.ferrazconectaapp.ui.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavController, profileViewModel: ProfileViewModel = hiltViewModel()) {
    val uiState by profileViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.fetchUserProfile()
    }

    when (val state = uiState) {
        is ProfileUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ProfileUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message)
            }
        }
        is ProfileUiState.Success -> {
            ProfileScreenContent(
                navController = navController,
                state = state,
                onNomeChange = profileViewModel::onNomeChange,
                onEmailChange = profileViewModel::onEmailChange,
                onTelefoneChange = profileViewModel::onTelefoneChange,
                onCpfChange = profileViewModel::onCpfChange,
                onDataNascimentoChange = profileViewModel::onDataNascimentoChange,
                onEnderecoChange = profileViewModel::onEnderecoChange,
                onResumoProfissionalChange = profileViewModel::onResumoProfissionalChange,
                onResumeUriChange = profileViewModel::onResumeUriChange,
                onSaveClick = profileViewModel::onSaveClick,
                onToggleEdit = profileViewModel::onToggleEdit,
                onSaveConfirmationShown = profileViewModel::onSaveConfirmationShown,
                onDeleteEscolaridade = profileViewModel::deleteEscolaridade,
                onDeleteExperiencia = profileViewModel::deleteExperienciaProfissional
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    navController: NavController,
    state: ProfileUiState.Success,
    onNomeChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onTelefoneChange: (String) -> Unit,
    onCpfChange: (String) -> Unit,
    onDataNascimentoChange: (String) -> Unit,
    onEnderecoChange: (String) -> Unit,
    onResumoProfissionalChange: (String) -> Unit,
    onResumeUriChange: (Uri?) -> Unit,
    onSaveClick: () -> Unit,
    onToggleEdit: () -> Unit,
    onSaveConfirmationShown: () -> Unit,
    onDeleteEscolaridade: (Escolaridade) -> Unit,
    onDeleteExperiencia: (ExperienciaProfissional) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val user = state.user
    var itemParaExcluir by remember { mutableStateOf<Any?>(null) }

    if (itemParaExcluir != null) {
        AlertDialog(
            onDismissRequest = { itemParaExcluir = null },
            title = { Text(text = "Confirmar Exclusão") },
            text = { Text("Você tem certeza que deseja excluir este item?") },
            confirmButton = {
                Button(
                    onClick = {
                        when (val item = itemParaExcluir) {
                            is Escolaridade -> onDeleteEscolaridade(item)
                            is ExperienciaProfissional -> onDeleteExperiencia(item)
                        }
                        itemParaExcluir = null
                    }
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                Button(onClick = { itemParaExcluir = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    val pickFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> onResumeUriChange(uri) }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    if (state.isEditing) {
                        IconButton(onClick = onSaveClick) {
                            Icon(Icons.Filled.Done, contentDescription = "Salvar")
                        }
                    } else {
                        IconButton(onClick = onToggleEdit) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(shape = CircleShape, modifier = Modifier.size(120.dp)) {
                    AsyncImage(
                        model = "https://thispersondoesnotexist.com/", // Imagem de placeholder
                        contentDescription = "Foto do Perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                Text("Informações Pessoais", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                OutlinedTextField(value = user.nome, onValueChange = onNomeChange, label = { Text("Nome Completo") }, modifier = Modifier.fillMaxWidth(), readOnly = !state.isEditing)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = user.email, onValueChange = onEmailChange, label = { Text("E-mail") }, modifier = Modifier.fillMaxWidth(), readOnly = !state.isEditing)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = user.telefone, onValueChange = onTelefoneChange, label = { Text("Telefone") }, modifier = Modifier.fillMaxWidth(), readOnly = !state.isEditing)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = user.cpf, onValueChange = onCpfChange, label = { Text("CPF") }, modifier = Modifier.fillMaxWidth(), readOnly = !state.isEditing)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = user.dataNascimento, onValueChange = onDataNascimentoChange, label = { Text("Data de Nascimento") }, modifier = Modifier.fillMaxWidth(), readOnly = !state.isEditing)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = user.endereco, onValueChange = onEnderecoChange, label = { Text("Endereço") }, modifier = Modifier.fillMaxWidth(), readOnly = !state.isEditing)
                Spacer(modifier = Modifier.height(24.dp))

                Text("Resumo Profissional", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                OutlinedTextField(value = user.resumoProfissional, onValueChange = onResumoProfissionalChange, label = { Text("Resumo Profissional") }, modifier = Modifier.fillMaxWidth(), readOnly = !state.isEditing, maxLines = 5)
                Spacer(modifier = Modifier.height(32.dp))

                Text("Currículo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }

            item {
                if (state.resumeUri != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AttachFile, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = state.resumeUri.lastPathSegment ?: "", maxLines = 1)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Button(onClick = { pickFileLauncher.launch("*/*") }, modifier = Modifier.fillMaxWidth(), enabled = state.isEditing) {
                    Text(if (state.resumeUri == null) "Adicionar Currículo" else "Alterar Currículo")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                SectionHeader(title = "Escolaridade", onAddClick = { navController.navigate(Routes.createFormEscolaridadeRoute()) }, isEditing = state.isEditing)
            }
            
            if (user.escolaridade.isEmpty()) {
                item { Text(text = "Nenhuma escolaridade adicionada.", modifier = Modifier.padding(vertical = 8.dp)) }
            } else {
                items(user.escolaridade) { escolaridade ->
                    EscolaridadeItem(
                        escolaridade = escolaridade, 
                        isEditing = state.isEditing, 
                        onEditClick = { 
                            navController.navigate(Routes.createFormEscolaridadeRoute(escolaridade.id))
                        },
                        onDeleteClick = { itemParaExcluir = escolaridade }
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                SectionHeader(title = "Experiência Profissional", onAddClick = { navController.navigate(Routes.createFormExperienciaRoute()) }, isEditing = state.isEditing)
            }

            if (user.experienciaProfissional.isEmpty()) {
                item { Text(text = "Nenhuma experiência profissional adicionada.", modifier = Modifier.padding(vertical = 8.dp)) }
            } else {
                items(user.experienciaProfissional) { experiencia ->
                    ExperienciaItem(
                        experiencia = experiencia, 
                        isEditing = state.isEditing, 
                        onEditClick = { 
                             navController.navigate(Routes.createFormExperienciaRoute(experiencia.id))
                        },
                        onDeleteClick = { itemParaExcluir = experiencia }
                    )
                }
            }
        }
    }

    LaunchedEffect(state.showSaveConfirmation) {
        if (state.showSaveConfirmation) {
            snackbarHostState.showSnackbar("Perfil salvo com sucesso!")
            onSaveConfirmationShown()
        }
    }
}

@Composable
fun SectionHeader(title: String, onAddClick: () -> Unit, isEditing: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        if (isEditing) {
            IconButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar")
            }
        }
    }
}

@Composable
fun EscolaridadeItem(escolaridade: Escolaridade, isEditing: Boolean, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = escolaridade.curso, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(text = "${escolaridade.instituicao} - ${escolaridade.nivel}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "${escolaridade.dataInicio} - ${escolaridade.dataFim}", style = MaterialTheme.typography.bodySmall)
            }
            if (isEditing) {
                Row {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Excluir")
                    }
                }
            }
        }
    }
}

@Composable
fun ExperienciaItem(experiencia: ExperienciaProfissional, isEditing: Boolean, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = experiencia.cargo, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(text = experiencia.empresa, style = MaterialTheme.typography.bodyMedium)
                Text(text = "${experiencia.dataInicio} - ${experiencia.dataFim}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = experiencia.descricao, style = MaterialTheme.typography.bodyMedium)
            }
            if (isEditing) {
                Row {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Excluir")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    FerrazConectaAppTheme {
        val user = User(
            nome = "Yuris Moreira",
            email = "yuris.moreira@example.com",
            telefone = "(11) 99999-9999",
            cpf = "123.456.789-00",
            dataNascimento = "01/01/1990",
            endereco = "Rua das Flores, 123, Ferraz de Vasconcelos - SP",
            resumoProfissional = "Desenvolvedor Android com 5 anos de experiência...",
            escolaridade = listOf(Escolaridade(id = "1", instituicao = "Universidade de São Paulo", curso = "Ciência da Computação", nivel = "Bacharelado", dataInicio = "2010", dataFim = "2014")),
            experienciaProfissional = listOf(ExperienciaProfissional(id = "1", empresa = "Google", cargo = "Engenheiro de Software", dataInicio = "2015", dataFim = "2020", descricao = "Trabalhei no app do Google..."))
        )
        ProfileScreenContent(
            navController = rememberNavController(),
            state = ProfileUiState.Success(user = user, isEditing = true),
            onNomeChange = {}, onEmailChange = {}, onTelefoneChange = {}, onCpfChange = {},
            onDataNascimentoChange = {}, onEnderecoChange = {}, onResumoProfissionalChange = {},
            onResumeUriChange = {}, onSaveClick = {}, onToggleEdit = {}, onSaveConfirmationShown = {},
            onDeleteEscolaridade = {}, onDeleteExperiencia = {}
        )
    }
}
