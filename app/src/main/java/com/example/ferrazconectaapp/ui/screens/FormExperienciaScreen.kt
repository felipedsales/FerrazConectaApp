package com.example.ferrazconectaapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ferrazconectaapp.ui.viewmodels.FormExperienciaViewModel
import com.example.ferrazconectaapp.ui.viewmodels.FormResult
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormExperienciaScreen(
    navController: NavController,
    viewModel: FormExperienciaViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.formResult.collectLatest {
            when(it) {
                is FormResult.SalvoComSucesso -> {
                    Toast.makeText(context, "Experiência salva com sucesso!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
                is FormResult.Erro -> {
                    Toast.makeText(context, it.mensagem, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Experiência Profissional") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.empresa,
                onValueChange = viewModel::onEmpresaChange,
                label = { Text("Empresa") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.cargo,
                onValueChange = viewModel::onCargoChange,
                label = { Text("Cargo") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.dataInicio,
                onValueChange = viewModel::onDataInicioChange,
                label = { Text("Data de Início") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.dataFim,
                onValueChange = viewModel::onDataFimChange,
                label = { Text("Data de Fim (ou 'Atual')") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.descricao,
                onValueChange = viewModel::onDescricaoChange,
                label = { Text("Descrição das atividades") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = viewModel::salvarExperiencia, modifier = Modifier.fillMaxWidth()) {
                Text("Salvar")
            }
        }
    }
}
