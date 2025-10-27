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

@Composable
fun Step3EmailInput(
    emailValue: String,
    onEmailChange: (String) -> Unit,
    onConfirm: () -> Unit
) {
    // Validação simples (pode ser aprimorada com regex)
    val isButtonEnabled = emailValue.contains("@") && emailValue.contains(".") && emailValue.length > 5

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Passo 3 de 4", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Agora, seu melhor e-mail",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = emailValue,
            // Permite caracteres comuns de email, mas sem validação complexa aqui
            onValueChange = onEmailChange,
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            enabled = isButtonEnabled // Habilita com validação básica
        ) {
            Text("Confirmar")
        }
    }
}