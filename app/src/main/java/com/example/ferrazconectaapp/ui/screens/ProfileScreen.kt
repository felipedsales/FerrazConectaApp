package com.example.ferrazconectaapp.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.ferrazconectaapp.ui.theme.FerrazConectaAppTheme
import com.example.ferrazconectaapp.ui.viewmodels.ProfileUiState
import com.example.ferrazconectaapp.ui.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavController, profileViewModel: ProfileViewModel = hiltViewModel()) {
    val uiState by profileViewModel.uiState.collectAsState()

    ProfileScreenContent(
        navController = navController,
        uiState = uiState,
        onNomeChange = profileViewModel::onNomeChange,
        onEmailChange = profileViewModel::onEmailChange,
        onTelefoneChange = profileViewModel::onTelefoneChange,
        onCpfChange = profileViewModel::onCpfChange,
        onEnderecoChange = profileViewModel::onEnderecoChange,
        onResumoProfissionalChange = profileViewModel::onResumoProfissionalChange,
        onResumeUriChange = profileViewModel::onResumeUriChange,
        onSaveClick = profileViewModel::onSaveClick,
        onToggleEdit = profileViewModel::onToggleEdit,
        onSaveConfirmationShown = profileViewModel::onSaveConfirmationShown
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    navController: NavController,
    uiState: ProfileUiState,
    onNomeChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onTelefoneChange: (String) -> Unit,
    onCpfChange: (String) -> Unit,
    onEnderecoChange: (String) -> Unit,
    onResumoProfissionalChange: (String) -> Unit,
    onResumeUriChange: (Uri?) -> Unit,
    onSaveClick: () -> Unit,
    onToggleEdit: () -> Unit,
    onSaveConfirmationShown: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

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
                    if (uiState.isEditing) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

            OutlinedTextField(
                value = uiState.nome,
                onValueChange = onNomeChange,
                label = { Text("Nome Completo") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !uiState.isEditing
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !uiState.isEditing
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.telefone,
                onValueChange = onTelefoneChange,
                label = { Text("Telefone") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !uiState.isEditing
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.cpf,
                onValueChange = onCpfChange,
                label = { Text("CPF") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !uiState.isEditing
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.endereco,
                onValueChange = onEnderecoChange,
                label = { Text("Endereço") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !uiState.isEditing
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Resumo Profissional", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            OutlinedTextField(
                value = uiState.resumoProfissional,
                onValueChange = onResumoProfissionalChange,
                label = { Text("Resumo Profissional") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !uiState.isEditing,
                maxLines = 5
            )
            Spacer(modifier = Modifier.height(32.dp))

            Text("Currículo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            if (uiState.resumeUri != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AttachFile, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = uiState.resumeUri.lastPathSegment ?: "", maxLines = 1)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            Button(
                onClick = { pickFileLauncher.launch("*/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.resumeUri == null) "Adicionar Currículo" else "Alterar Currículo")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    LaunchedEffect(uiState.showSaveConfirmation) {
        if (uiState.showSaveConfirmation) {
            snackbarHostState.showSnackbar("Perfil salvo com sucesso!")
            onSaveConfirmationShown()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    FerrazConectaAppTheme {
        ProfileScreenContent(
            navController = rememberNavController(),
            uiState = ProfileUiState(),
            onNomeChange = {},
            onEmailChange = {},
            onTelefoneChange = {},
            onCpfChange = {},
            onEnderecoChange = {},
            onResumoProfissionalChange = {},
            onResumeUriChange = {},
            onSaveClick = {},
            onToggleEdit = {},
            onSaveConfirmationShown = {}
        )
    }
}
