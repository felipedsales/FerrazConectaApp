package com.example.ferrazconectaapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ferrazconectaapp.ui.util.CpfVisualTransformation
import com.example.ferrazconectaapp.ui.util.DateVisualTransformation
import com.example.ferrazconectaapp.ui.util.PhoneVisualTransformation
import com.example.ferrazconectaapp.ui.viewmodels.CadastroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroScreen(
    onNavigateBack: () -> Unit,
    cadastroViewModel: CadastroViewModel = viewModel()
) {
    val cadastroSuccess = cadastroViewModel.cadastroSuccess
    LaunchedEffect(cadastroSuccess) {
        if (cadastroSuccess.value) {
            onNavigateBack()
        }
    }

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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = android.R.drawable.ic_dialog_info),
                contentDescription = "Logo Ferraz Conecta",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = cadastroViewModel.nome,
                onValueChange = { cadastroViewModel.onNomeChange(it) },
                label = { Text("Nome Completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = cadastroViewModel.email,
                onValueChange = { cadastroViewModel.onEmailChange(it) },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                isError = cadastroViewModel.emailError != null,
                supportingText = { 
                    cadastroViewModel.emailError?.let { Text(it) } 
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = cadastroViewModel.cpf,
                onValueChange = { cadastroViewModel.onCpfChange(it) },
                label = { Text("CPF") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                visualTransformation = CpfVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = cadastroViewModel.dataNascimento,
                onValueChange = { cadastroViewModel.onDataNascimentoChange(it) },
                label = { Text("Data de Nascimento") },
                placeholder = { Text("DD/MM/AAAA") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                visualTransformation = DateVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = cadastroViewModel.telefone,
                onValueChange = { cadastroViewModel.onTelefoneChange(it) },
                label = { Text("Telefone") },
                placeholder = { Text("(11) 99999-9999") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                visualTransformation = PhoneVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = cadastroViewModel.senha,
                onValueChange = { cadastroViewModel.onSenhaChange(it) },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = cadastroViewModel.confirmarSenha,
                onValueChange = { cadastroViewModel.onConfirmarSenhaChange(it) },
                label = { Text("Confirmar Senha") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = cadastroViewModel.confirmarSenhaError != null,
                supportingText = { 
                    cadastroViewModel.confirmarSenhaError?.let { Text(it) } 
                }
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { cadastroViewModel.onCadastroClick() },
                modifier = Modifier.fillMaxWidth(),
                enabled = cadastroViewModel.isFormValid
            ) {
                Text("Cadastrar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CadastroScreenPreview() {
    CadastroScreen(onNavigateBack = {})
}
