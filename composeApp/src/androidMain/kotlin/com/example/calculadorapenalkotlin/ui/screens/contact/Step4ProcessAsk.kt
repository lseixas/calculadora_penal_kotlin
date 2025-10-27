package com.example.calculadorapenalkotlin.ui.screens.contact // Pacote correto

// Imports necessários
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Step4ProcessAsk( // Renomeado para seguir padrão
    onConfirm: () -> Unit,
    onDeny: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Passo 4 de 4 (Opcional)", style = MaterialTheme.typography.titleMedium) // Ajustado
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Gostaria de inserir o número do processo (opcional)?", // Ajustado
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onConfirm, // Vai para o Step 5
                modifier = Modifier.weight(1f)
            ) {
                Text("Sim")
            }
            // Usando OutlinedButton para o "Não" para dar menos ênfase
            OutlinedButton(
                onClick = onDeny, // Finaliza e volta para Home
                modifier = Modifier.weight(1f)
            ) {
                Text("Não") // Texto mais claro
            }
        }
    }
}