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
import com.example.calculadorapenalkotlin.ui.util.transformations.PhoneVisualTransformation // Importa a transformação

@Composable
fun Step2WhatsappInput(
    whatsappValue: String,
    onWhatsappChange: (String) -> Unit,
    onConfirm: () -> Unit
) {
    // Validação para habilitar o botão
    val isButtonEnabled = whatsappValue.length == 11

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Passo 2 de 4", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Agora, digite seu WhatsApp",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = whatsappValue,
            onValueChange = { newValue ->
                // Permite apenas dígitos e limita a 11
                if (newValue.all { it.isDigit() } && newValue.length <= 11) {
                    onWhatsappChange(newValue)
                }
            },
            label = { Text("Telefone (com DDD)") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PhoneVisualTransformation(), // Aplica a máscara
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            enabled = isButtonEnabled // Botão habilitado apenas com 11 dígitos
        ) {
            Text("Confirmar")
        }
    }
}