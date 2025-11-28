package com.example.ferrazconectaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ferrazconectaapp.ui.theme.FerrazConectaAppTheme
import com.example.ferrazconectaapp.ui.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    onNavigateToCadastro: () -> Unit,
    onLoginSuccess: () -> Unit, // Callback para navegar após login bem-sucedido
    loginViewModel: LoginViewModel = viewModel()
) {

    // Observa o estado de sucesso do login
    val loginSuccess = loginViewModel.loginSuccess
    LaunchedEffect(loginSuccess) {
        if (loginSuccess.value) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Logo
        Icon(
            imageVector = Icons.Filled.BusinessCenter,
            contentDescription = "Logo Ferraz Conecta",
            modifier = Modifier.size(150.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))

        // 2. Título
        Text(text = "Login", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))

        // 3. E-mail
        OutlinedTextField(
            value = loginViewModel.email,
            onValueChange = { loginViewModel.onEmailChange(it) },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 4. Senha
        OutlinedTextField(
            value = loginViewModel.password,
            onValueChange = { loginViewModel.onPasswordChange(it) },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (loginViewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            trailingIcon = {
                val image = if (loginViewModel.passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (loginViewModel.passwordVisible) "Ocultar senha" else "Mostrar senha"
                IconButton(onClick = { loginViewModel.togglePasswordVisibility() }) {
                    Icon(imageVector = image, description)
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 5. Esqueci minha senha
        TextButton(
            onClick = { /* FAZER */ },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Esqueci minha senha?")
        }
        Spacer(modifier = Modifier.height(24.dp))

        // 6. Botão Entrar
        Button(
            onClick = { loginViewModel.onLoginClick() }, // Chama a função de login
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }
        Spacer(modifier = Modifier.height(24.dp))

        // 7. Separador
        Text("ou entre com")
        Spacer(modifier = Modifier.height(24.dp))

        // 8. Login Social
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedButton(
                onClick = { /* FAZER */ },
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle, 
                    contentDescription = "Login com Google",
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedButton(
                onClick = { /* FAZER */ },
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Login com LinkedIn",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        // 9. Link para Cadastro
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Não possui conta? ")
            TextButton(onClick = onNavigateToCadastro) {
                Text("Cadastre-se")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    FerrazConectaAppTheme {
        LoginScreen(onNavigateToCadastro = {}, onLoginSuccess = {})
    }
}
