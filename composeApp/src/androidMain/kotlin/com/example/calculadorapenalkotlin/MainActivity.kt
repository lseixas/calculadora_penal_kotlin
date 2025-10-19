package com.example.calculadorapenalkotlin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calculadorapenalkotlin.ui.theme.CalculadoraPenalKotlinTheme
import com.example.calculadorapenalkotlin.screenEnums.MainScreenEnum
import com.example.calculadorapenalkotlin.screenEnums.StepScreenEnum
import com.example.calculadorapenalkotlin.transformations.PhoneVisualTransformation
import com.example.calculadorapenalkotlin.userStateEnum.UserStateEnum
import org.lseixas.domain.enum.StatusApenado
import org.lseixas.domain.enum.TipoCrime
import org.lseixas.domain.objects.UsuarioES
import org.lseixas.usecase.calcularBeneficios

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculadoraPenalKotlinTheme { // Use o tema do seu projeto aqui

                var currentScreen by remember { mutableStateOf(MainScreenEnum.CONTACT_FORM) }
                var userStateEnum by remember { mutableStateOf(UserStateEnum.HAS_FILED)}

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    when (currentScreen) {
                        MainScreenEnum.HOME -> HomePage(
                            // Passamos uma função para que a HomePage possa pedir para mudar de tela
                            onNavigateToContactForm = { currentScreen = MainScreenEnum.CONTACT_FORM }
                        )
                        MainScreenEnum.CONTACT_FORM -> MultiStepScreen(
                            // Passamos uma função para que o formulário possa voltar para a Home
                            onNavigateToHome = { currentScreen = MainScreenEnum.HOME }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomePage(onNavigateToContactForm: () -> Unit) {
    // States para cada um dos inputs da calculadora
    var penaAnos by remember { mutableStateOf("") }
    var penaMeses by remember { mutableStateOf("") }
    var penaDias by remember { mutableStateOf("") }
    var dataInicio by remember { mutableStateOf("") }
    var detracao by remember { mutableStateOf("") }
    // Para os RadioButtons, guardamos a opção selecionada
    var tipoCrimeSelecionado by remember { mutableStateOf("Comum") }
    var statusApenadoSelecionado by remember { mutableStateOf("Primário") }

    var calculationResult by remember { mutableStateOf<UsuarioES?>(null) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                // Adiciona uma barra de rolagem caso o conteúdo não caiba na tela
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Calculadora Penal", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(24.dp))

            // --- INPUTS DA PENA ---
            Text("Pena Total", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = penaAnos,
                    onValueChange = { if (it.all { char -> char.isDigit() }) penaAnos = it },
                    label = { Text("Anos") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = penaMeses,
                    onValueChange = { if (it.all { char -> char.isDigit() }) penaMeses = it },
                    label = { Text("Meses") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = penaDias,
                    onValueChange = { if (it.all { char -> char.isDigit() }) penaDias = it },
                    label = { Text("Dias") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // --- OUTROS INPUTS ---
            OutlinedTextField(
                value = dataInicio,
                onValueChange = { dataInicio = it },
                label = { Text("Data de Início (DD/MM/AAAA)") },
                modifier = Modifier.fillMaxWidth()
                // TODO: Idealmente, usar um DatePickerDialog aqui
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = detracao,
                onValueChange = { if (it.all { char -> char.isDigit() }) detracao = it },
                label = { Text("Detração (dias, se houver)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- INPUTS DE MÚLTIPLA ESCOLHA ---
            Text("Tipo de Crime", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())
            Row(modifier = Modifier.fillMaxWidth()) {
                RadioButtonOption(
                    text = "Comum",
                    selected = tipoCrimeSelecionado == "Comum",
                    onClick = { tipoCrimeSelecionado = "Comum" }
                )
                RadioButtonOption(
                    text = "Hediondo",
                    selected = tipoCrimeSelecionado == "Hediondo",
                    onClick = { tipoCrimeSelecionado = "Hediondo" }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("Status do Apenado", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())
            Row(modifier = Modifier.fillMaxWidth()) {
                RadioButtonOption(
                    text = "Primário",
                    selected = statusApenadoSelecionado == "Primário",
                    onClick = { statusApenadoSelecionado = "Primário" }
                )
                RadioButtonOption(
                    text = "Reincidente",
                    selected = statusApenadoSelecionado == "Reincidente",
                    onClick = { statusApenadoSelecionado = "Reincidente" }
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {

                    calculationResult = null

                    // --- ETAPA 1: COLETAR DADOS (sem conversão de data) ---
                    val anos = penaAnos.toIntOrNull() ?: 0
                    val meses = penaMeses.toIntOrNull() ?: 0
                    val dias = penaDias.toIntOrNull() ?: 0
                    val diasDetracao = detracao.toIntOrNull() ?: 0

                    val tipoCrime = if (tipoCrimeSelecionado == "Comum") TipoCrime.COMUM else TipoCrime.HEDIONDO_EQUIPARADO
                    val statusApenado = if (statusApenadoSelecionado == "Primário") StatusApenado.PRIMARIO else StatusApenado.REINCIDENTE

                    // --- ETAPA 2: CHAMAR A LÓGICA ---
                    // Criamos o objeto UsuarioES com os tipos simples que ele espera
                    val entrada = UsuarioES(
                        penaAnos = anos,
                        penaMeses = meses,
                        penaDias = dias,
                        dataInicioPena = dataInicio, // <-- PASSAMOS A STRING DIRETAMENTE!
                        detracaoDias = diasDetracao,
                        tipoCrime = tipoCrime,
                        statusApenado = statusApenado
                    )

                    // A função 'calcularBeneficios' agora é a única responsável
                    // pela lógica de conversão e cálculo.
                    calculationResult = calcularBeneficios(entrada)
                    println("a")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calcular Benefícios")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onNavigateToContactForm, // Chama a função para navegar
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Quero Falar com um Advogado")
            }

            AnimatedVisibility(
                visible = calculationResult != null,
                enter = slideInVertically { fullHeight -> fullHeight / 2 } + fadeIn(),
                exit = slideOutVertically { fullHeight -> fullHeight } + fadeOut()
            ) {
                // Quando um novo resultado aparece, rolamos a tela para ele
                LaunchedEffect(calculationResult) {
                    // Rola suavemente para o final da página para garantir que o card seja visível
                    scrollState.animateScrollTo(scrollState.maxValue)
                }

                // O Card de resultado, agora como um item normal da Column
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp), // Um espaço para separar dos botões
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Resultados do Cálculo", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Conteúdo do resultado...
                        Text("Semiaberto: ${calculationResult?.dataProgressaoSemiaberto ?: "N/A"}", modifier = Modifier.fillMaxWidth())
                        Text("Aberto: ${calculationResult?.dataProgressaoAberto ?: "N/A"}", modifier = Modifier.fillMaxWidth())
                        Text("Livramento: ${calculationResult?.dataLivramentoCondicional ?: "N/A"}", modifier = Modifier.fillMaxWidth())

                        if (calculationResult?.erro != null) {
                            Text("Erro: ${calculationResult?.erro}", color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }
        }
    }
}

// Composable reutilizável para os botões de rádio, para não repetir código
@Composable
fun RowScope.RadioButtonOption(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(text = text, modifier = Modifier.padding(start = 4.dp))
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MultiStepScreen( onNavigateToHome: () -> Unit ) {
    // 1. STATE para controlar qual passo (tela) está visível
    var currentStep by remember { mutableStateOf(StepScreenEnum.NAMESTEP_1) }

    // States para guardar o que o usuário digita em cada campo
    var nome by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var numeroProcesso by remember { mutableStateOf("") }

    // 2. O componente mágico para a animação de "arrastar" para o lado
    AnimatedContent(
        targetState = currentStep,
        label = "StepAnimation",
        transitionSpec = {
            // Define a animação: a nova tela entra da direita e a antiga sai para a esquerda
            slideInHorizontally { fullWidth -> fullWidth } togetherWith
                    slideOutHorizontally { fullWidth -> -fullWidth }
        }
    ) { step ->
        // 3. O 'when' decide qual tela mostrar com base no estado 'currentStep'
        when (step) {
            StepScreenEnum.NAMESTEP_1 -> Step1_NameInput(
                nameValue = nome,
                onNameChange = { nome = it },
                onConfirm = {
                    // Ao confirmar, simplesmente mudamos o estado para 2, e a animação acontece!
                    currentStep = StepScreenEnum.PHONESTEP_2
                }
            )
            StepScreenEnum.PHONESTEP_2 -> Step2_WhatsappInput(
                whatsappValue = whatsapp,
                onWhatsappChange = { whatsapp = it },
                onConfirm = {
                    // Aqui você iria para o passo 3 ou finalizaria o fluxo
                    // Por enquanto, vamos voltar para o passo 1 para criar um loop
                    currentStep = StepScreenEnum.EMAILSTEP_3
                }
            )
            StepScreenEnum.EMAILSTEP_3 -> Step3_EmailInput(
                emailValue = email,
                onEmailChange = { email = it },
                onConfirm = {
                    currentStep = StepScreenEnum.PNASK_4
                }
            )
            StepScreenEnum.PNASK_4 -> Step4_NumeroProcessoAsk(
                onConfirm = {
                    currentStep = StepScreenEnum.PNINPUT_5 //mudar para tela de input processo

                },
                onDeny = {
                    onNavigateToHome()
                    currentStep = StepScreenEnum.NAMESTEP_1 //mudar para tela de inicio
                }
            )
            StepScreenEnum.PNINPUT_5 -> Step5_NumeroProcessoInput(
                numeroProcessoValue = numeroProcesso,
                onNumeroProcessoChange = { numeroProcesso = it },
                onConfirm = {
                    onNavigateToHome()
                    currentStep = StepScreenEnum.NAMESTEP_1 //mudar para tela de inicio
                },
            )
        }
    }
}

@Composable
fun Step1_NameInput(nameValue: String, onNameChange: (String) -> Unit, onConfirm: () -> Unit) {
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
                if (newValue.all { it.isLetter() || it.isWhitespace() }) {
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
            onClick = onConfirm, // Chama a função que recebemos para mudar de tela
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar")
        }
    }
}

@Composable
fun Step2_WhatsappInput(whatsappValue: String, onWhatsappChange: (String) -> Unit, onConfirm: () -> Unit) {

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
                if (newValue.all { it.isDigit() } && newValue.length <= 11) {
                    onWhatsappChange(newValue)
                }
            },
            label = { Text("Telefone") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PhoneVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            enabled = isButtonEnabled
        ) {
            Text("Confirmar")
        }
    }
}

@Composable
fun Step3_EmailInput(emailValue: String, onEmailChange: (String) -> Unit, onConfirm: () -> Unit) {

    val isButtonEnabled = emailValue.contains("@") && emailValue.contains(".")

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
            onValueChange = { newValue ->
                if (newValue.all { it.isLetterOrDigit() || it == '@' || it == '.' }) {
                    onEmailChange(newValue)
                }
            },
            label = { Text("e-mail") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            enabled = isButtonEnabled
        ) {
            Text("Confirmar")
        }
    }
}

@Composable
fun Step4_NumeroProcessoAsk(
    onConfirm: () -> Unit,
    onDeny: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Passo 4 de 4", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Gostaria de inserir o número do processo?",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onConfirm,
                modifier = Modifier.weight(1f)
            ) {
                Text("Sim")
            }
            Button(
                onClick = onDeny,
                modifier = Modifier.weight(1f)
            ) {
                Text("Não")
            }
        }
    }
}

@Composable
fun Step5_NumeroProcessoInput(
    numeroProcessoValue: String,
    onNumeroProcessoChange: (String) -> Unit,
    onConfirm: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Passo 4 de 4", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Gostaria de inserir o número do processo?",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = numeroProcessoValue,
            onValueChange = onNumeroProcessoChange,
            label = { Text("número do processo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sim")
        }
    }
}
