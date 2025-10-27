package com.example.calculadorapenalkotlin.ui.screens.contact // Pacote correto

// Imports necessários para este passo
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Step1NameInput(nameValue: String, onNameChange: (String) -> Unit, onConfirm: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Passo 1 de 4", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Digite seu Nome",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = nameValue,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.all { it.isLetter() || it.isWhitespace() }) { // Permite apagar
                    onNameChange(newValue)
                }
            },
            label = { Text("Nome completo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            // Desabilita o botão se o nome estiver vazio
            enabled = nameValue.isNotBlank()
        ) {
            Text("Confirmar")
        }
    }
}