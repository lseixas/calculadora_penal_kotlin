package com.example.calculadorapenalkotlin.ui.screens.contact // Pacote correto

// Imports necessários
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
// TODO: Adicionar uma VisualTransformation para número de processo, se desejar

@Composable
fun Step5ProcessInput( // Renomeado para seguir padrão
    numeroProcessoValue: String,
    onNumeroProcessoChange: (String) -> Unit,
    onConfirm: () -> Unit
) {
    // Validação simples: botão habilitado se algo for digitado
    val isButtonEnabled = numeroProcessoValue.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Passo Final (Opcional)", style = MaterialTheme.typography.titleMedium) // Ajustado
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Digite o número do processo", // Ajustado
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = numeroProcessoValue,
            onValueChange = onNumeroProcessoChange,
            label = { Text("Número do processo") },
            modifier = Modifier.fillMaxWidth(),
            // Você pode querer adicionar máscara e validação aqui
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Ou Text?
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onConfirm, // Finaliza e volta para Home
            modifier = Modifier.fillMaxWidth(),
            enabled = isButtonEnabled
        ) {
            Text("Finalizar") // Texto mais claro
        }
    }
}