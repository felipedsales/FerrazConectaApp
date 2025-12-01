package com.example.ferrazconectaapp.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ferrazconectaapp.R
import com.example.ferrazconectaapp.data.repository.AuthRepository
import com.example.ferrazconectaapp.di.FirebaseModule
import com.example.ferrazconectaapp.ui.theme.FerrazConectaAppTheme
import com.example.ferrazconectaapp.ui.util.CpfVisualTransformation
import com.example.ferrazconectaapp.ui.util.DateVisualTransformation
import com.example.ferrazconectaapp.ui.util.PhoneVisualTransformation
import com.example.ferrazconectaapp.ui.viewmodels.CadastroUiState
import com.example.ferrazconectaapp.ui.viewmodels.CadastroViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun CadastroScreen(
    onNavigateBack: () -> Unit,
    onLoginSuccess: () -> Unit,
    cadastroViewModel: CadastroViewModel = hiltViewModel()
) {
    val uiState by cadastroViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is CadastroUiState.Success -> {
                Toast.makeText(context, "Cadastro realizado com sucesso! Faça o login para continuar.", Toast.LENGTH_LONG).show()
                onLoginSuccess()
                cadastroViewModel.resetUiState()
            }
            is CadastroUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                cadastroViewModel.resetUiState()
            }
            is CadastroUiState.Idle -> {}
        }
    }

    CadastroScreenContent(
        onNavigateBack = onNavigateBack,
        viewModel = cadastroViewModel,
        onCadastroClick = { cadastroViewModel.onCadastroClick() },
        onGoogleSignInClick = { idToken ->
            cadastroViewModel.signInWithGoogleToken(idToken)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroScreenContent(
    onNavigateBack: () -> Unit,
    viewModel: CadastroViewModel,
    onCadastroClick: () -> Unit,
    onGoogleSignInClick: (String) -> Unit
) {
    val context = LocalContext.current

    // Configure Google Sign-In
    val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(stringResource(id = R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    account?.idToken?.let {
                        onGoogleSignInClick(it)
                    } ?: run {
                        Toast.makeText(context, "Falha ao obter o token do Google", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: ApiException) {
                    Toast.makeText(context, "Login com Google falhou: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Cadastro") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.BusinessCenter,
                contentDescription = "Logo Ferraz Conecta",
                modifier = Modifier.padding(bottom = 16.dp), 
                tint = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = viewModel.nome.value,
                onValueChange = viewModel::onNomeChange,
                label = { Text("Nome Completo") },
                modifier = Modifier.fillMaxWidth().onFocusChanged { if (!it.isFocused) viewModel.validateNome() },
                singleLine = true,
                isError = viewModel.nome.error != null,
                supportingText = { viewModel.nome.error?.let { Text(it) } }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.email.value,
                onValueChange = viewModel::onEmailChange,
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth().onFocusChanged { if (!it.isFocused) viewModel.validateEmail() },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                isError = viewModel.email.error != null,
                supportingText = { viewModel.email.error?.let { Text(it) } }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.cpf.value,
                onValueChange = viewModel::onCpfChange,
                label = { Text("CPF") },
                modifier = Modifier.fillMaxWidth().onFocusChanged { if (!it.isFocused) viewModel.validateCpf() },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = viewModel.cpf.error != null,
                supportingText = { viewModel.cpf.error?.let { Text(it) } },
                visualTransformation = CpfVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.dataNascimento.value,
                onValueChange = viewModel::onDataNascimentoChange,
                label = { Text("Data de Nascimento") },
                placeholder = { Text("DD/MM/AAAA") },
                modifier = Modifier.fillMaxWidth().onFocusChanged { if (!it.isFocused) viewModel.validateDataNascimento() },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = viewModel.dataNascimento.error != null,
                supportingText = { viewModel.dataNascimento.error?.let { Text(it) } },
                visualTransformation = DateVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.telefone.value,
                onValueChange = viewModel::onTelefoneChange,
                label = { Text("Telefone") },
                placeholder = { Text("(11) 99999-9999") },
                modifier = Modifier.fillMaxWidth().onFocusChanged { if (!it.isFocused) viewModel.validateTelefone() },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                isError = viewModel.telefone.error != null,
                supportingText = { viewModel.telefone.error?.let { Text(it) } },
                visualTransformation = PhoneVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.senha.value,
                onValueChange = viewModel::onSenhaChange,
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth().onFocusChanged { if (!it.isFocused) viewModel.validateSenha() },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = viewModel.senha.error != null,
                supportingText = { viewModel.senha.error?.let { Text(it) } }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.confirmarSenha.value,
                onValueChange = viewModel::onConfirmarSenhaChange,
                label = { Text("Confirmar Senha") },
                modifier = Modifier.fillMaxWidth().onFocusChanged { if (!it.isFocused) viewModel.validateSenha() },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = viewModel.confirmarSenha.error != null,
                supportingText = { viewModel.confirmarSenha.error?.let { Text(it) } }
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onCadastroClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cadastrar")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(modifier = Modifier.weight(1f))
                Text(" ou ", modifier = Modifier.padding(horizontal = 8.dp))
                Divider(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { googleSignInLauncher.launch(googleSignInClient.signInIntent) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar com o Google")
            }
            
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CadastroScreenPreview() {
    FerrazConectaAppTheme {
        // This preview will not work correctly with the new Hilt and Google Sign-In logic.
        // It requires a more complex setup to mock these dependencies.
        val fakeAuth = AuthRepository(FirebaseModule.provideFirebaseAuth())
        CadastroScreenContent(
            onNavigateBack = {},
            viewModel = CadastroViewModel(fakeAuth),
            onCadastroClick = {},
            onGoogleSignInClick = {}
        )
    }
}
