package com.example.ferrazconectaapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ferrazconectaapp.data.model.Vaga

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VagaListItem(
    vaga: Vaga, 
    onClick: () -> Unit,
    status: String? = null,
    onDesistirClick: () -> Unit = {}
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = vaga.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = vaga.empresa,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = vaga.local,
                style = MaterialTheme.typography.bodySmall
            )

            status?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Status: $it",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDesistirClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Desistir da Vaga")
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Default")
@Composable
fun VagaListItemPreview() {
    VagaListItem(
        vaga = Vaga(
            id = 1,
            titulo = "Desenvolvedor Android Jr.",
            empresa = "Prefeitura de Ferraz de Vasconcelos",
            descricao = "Descrição completa da vaga aqui.",
            local = "Ferraz de Vasconcelos, SP"
        ),
        onClick = {}
    )
}

@Preview(showBackground = true, name = "Com Status")
@Composable
fun VagaListItemWithStatusPreview() {
    VagaListItem(
        vaga = Vaga(
            id = 1,
            titulo = "Desenvolvedor Android Jr.",
            empresa = "Prefeitura de Ferraz de Vasconcelos",
            descricao = "Descrição completa da vaga aqui.",
            local = "Ferraz de Vasconcelos, SP"
        ),
        onClick = {},
        status = "Em processo",
        onDesistirClick = {}
    )
}
