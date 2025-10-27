package com.example.calculadorapenalkotlin.ui.screens.home // Pacote correto

// Imports necessários para esta tela
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calculadorapenalkotlin.ui.components.RadioButtonOption // Importa o componente reutilizável
import com.example.calculadorapenalkotlin.ui.components.ResultContent // Importa o componente reutilizável
import com.example.calculadorapenalkotlin.ui.util.transformations.DMYVisualTransformation // Importa a transformação de data
import kotlinx.coroutines.launch // Para rolar a tela
import org.lseixas.domain.enum.StatusApenado
import org.lseixas.domain.enum.TipoCrime
import org.lseixas.domain.objects.UsuarioES
import org.lseixas.usecase.calcularBeneficios

@Composable
fun HomePage(onNavigateToContactForm: () -> Unit) {
    // States para inputs
    var penaAnos by remember { mutableStateOf("") }
    var penaMeses by remember { mutableStateOf("") }
    var penaDias by remember { mutableStateOf("") }
    var dataInicio by remember { mutableStateOf("") }
    var detracao by remember { mutableStateOf("") }
    var tipoCrimeSelecionado by remember { mutableStateOf("Comum") } // "Comum" ou "Hediondo"
    var statusApenadoSelecionado by remember { mutableStateOf("Primário") } // "Primário" ou "Reincidente"

    // --- NOVOS STATES CONDICIONAIS ---
    var cometidoComViolencia by remember { mutableStateOf(false) } // Só relevante se tipoCrimeSelecionado == "Comum"
    var resultadoMorte by remember { mutableStateOf(false) }     // Só relevante se tipoCrimeSelecionado == "Hediondo"

    // States para resultado
    var calculationResult by remember { mutableStateOf<UsuarioES?>(null) }
    var showResultDialog by remember { mutableStateOf(false) }
    var confirmedResult by remember { mutableStateOf<UsuarioES?>(null) }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Calculadora Penal", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))

        // --- INPUTS DA PENA ---
        Text("Pena Total", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField( /* Anos */ value = penaAnos, onValueChange = { if (it.isEmpty() || it.all { c -> c.isDigit() }) penaAnos = it }, label = { Text("Anos") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
            OutlinedTextField( /* Meses */ value = penaMeses, onValueChange = { if (it.isEmpty() || it.all { c -> c.isDigit() }) penaMeses = it }, label = { Text("Meses") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
            OutlinedTextField( /* Dias */ value = penaDias, onValueChange = { if (it.isEmpty() || it.all { c -> c.isDigit() }) penaDias = it }, label = { Text("Dias") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // --- OUTROS INPUTS ---
        OutlinedTextField( // Data de Início
            value = dataInicio,
            onValueChange = { if (it.all { c -> c.isDigit() } && it.length <= 8) dataInicio = it },
            label = { Text("Data de Início (DD/MM/AAAA)") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = DMYVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField( // Detração
            value = detracao,
            onValueChange = { if (it.isEmpty() || it.all { c -> c.isDigit() }) detracao = it },
            label = { Text("Detração (dias, se houver)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- TIPO DE CRIME E CHECKBOXES CONDICIONAIS ---
        Text("Tipo de Crime", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())
        Row(modifier = Modifier.fillMaxWidth()) {
            RadioButtonOption(
                text = "Comum",
                selected = tipoCrimeSelecionado == "Comum",
                onClick = {
                    tipoCrimeSelecionado = "Comum"
                    resultadoMorte = false // Reseta a outra opção condicional
                }
            )
            RadioButtonOption(
                text = "Hediondo/Equiparado", // Texto mais preciso
                selected = tipoCrimeSelecionado == "Hediondo",
                onClick = {
                    tipoCrimeSelecionado = "Hediondo"
                    cometidoComViolencia = false // Reseta a outra opção condicional
                }
            )
        }

        // Checkbox condicional para Crime Comum
        AnimatedVisibility(visible = tipoCrimeSelecionado == "Comum") {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = cometidoComViolencia,
                    onCheckedChange = { cometidoComViolencia = it }
                )
                Text(
                    text = "Cometido com violência ou grave ameaça?",
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Checkbox condicional para Crime Hediondo
        AnimatedVisibility(visible = tipoCrimeSelecionado == "Hediondo") {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = resultadoMorte,
                    onCheckedChange = { resultadoMorte = it }
                )
                Text(
                    text = "Teve resultado morte?",
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // --- STATUS DO APENADO ---
        Text("Status do Apenado", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())
        Row(modifier = Modifier.fillMaxWidth()) {
            RadioButtonOption(text = "Primário", selected = statusApenadoSelecionado == "Primário", onClick = { statusApenadoSelecionado = "Primário" })
            RadioButtonOption(text = "Reincidente", selected = statusApenadoSelecionado == "Reincidente", onClick = { statusApenadoSelecionado = "Reincidente" })
        }
        Spacer(modifier = Modifier.height(32.dp))

        // --- BOTÕES DE AÇÃO ---
        Button(
            onClick = {
                confirmedResult = null
                calculationResult = null // Reset temporário

                val anos = penaAnos.toIntOrNull() ?: 0
                val meses = penaMeses.toIntOrNull() ?: 0
                val dias = penaDias.toIntOrNull() ?: 0
                val diasDetracao = detracao.toIntOrNull() ?: 0

                // ✅ LÓGICA ATUALIZADA PARA TIPO DE CRIME
                val tipoCrime: TipoCrime = when (tipoCrimeSelecionado) {
                    "Comum" -> if (cometidoComViolencia) TipoCrime.VIOLENCIA_AMEACA else TipoCrime.COMUM
                    "Hediondo" -> TipoCrime.HEDIONDO_EQUIPARADO
                    else -> TipoCrime.COMUM // Fallback, não deve acontecer
                }
                val statusApenado = if (statusApenadoSelecionado == "Primário") StatusApenado.PRIMARIO else StatusApenado.REINCIDENTE

                val entrada = UsuarioES(
                    penaAnos = anos, penaMeses = meses, penaDias = dias,
                    dataInicioPena = dataInicio, detracaoDias = diasDetracao,
                    tipoCrime = tipoCrime, // Passa o enum correto
                    statusApenado = statusApenado,
                    ehHediondoComMorte = resultadoMorte // Passa o boolean do checkbox
                )
                calculationResult = calcularBeneficios(entrada)

                if (calculationResult != null) {
                    showResultDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Calcular Benefícios") }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onNavigateToContactForm,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Quero Falar com um Advogado") }

        // --- CARD INFERIOR (PERMANENTE APÓS CONFIRMAÇÃO) ---
        AnimatedVisibility(
            visible = confirmedResult != null,
            enter = slideInVertically { h -> h / 2 } + fadeIn(),
            exit = slideOutVertically { h -> h } + fadeOut()
        ) {
            LaunchedEffect(confirmedResult) {
                if (confirmedResult != null) {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                ResultContent(result = confirmedResult) // Reutiliza o componente
            }
        }
    } // Fim da Column principal

    // --- DIALOG (POPUP) ---
    if (showResultDialog && calculationResult != null) {
        AlertDialog(
            onDismissRequest = { showResultDialog = false },
            title = { Text("Resultados do Cálculo", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
            text = { ResultContent(result = calculationResult) }, // Reutiliza o componente
            confirmButton = {
                Button(onClick = {
                    confirmedResult = calculationResult
                    showResultDialog = false
                }) { Text("Confirmar Vista") }
            }
        )
    }
}