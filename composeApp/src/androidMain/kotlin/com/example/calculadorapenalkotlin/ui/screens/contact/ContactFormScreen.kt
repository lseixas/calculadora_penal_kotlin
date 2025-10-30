package com.example.calculadorapenalkotlin.ui.screens.contact // Pacote correto

// Imports necessários
import androidx.compose.animation.*
import androidx.compose.runtime.*
import com.example.calculadorapenalkotlin.navigation.ContactStep // Importa o enum (anterior StepScreenEnum)
import com.example.calculadorapenalkotlin.repositories.ContactRepository


// Renomeado de MultiStepScreen para maior clareza
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ContactFormScreen(
    onNavigateToHome: () -> Unit,
    contactRepository: ContactRepository) {
    var currentStep by remember { mutableStateOf(ContactStep.NAME) } // Assumindo que você renomeou o enum e os valores

    // States para os dados do formulário
    var nome by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var numeroProcesso by remember { mutableStateOf("") }

    AnimatedContent(
        targetState = currentStep,
        label = "StepAnimation",
        transitionSpec = {
            slideInHorizontally { fullWidth -> fullWidth } togetherWith
                    slideOutHorizontally { fullWidth -> -fullWidth }
        }
    ) { step ->
        when (step) {
            ContactStep.NAME -> Step1NameInput(
                nameValue = nome,
                onNameChange = { nome = it },
                onConfirm = { currentStep = ContactStep.PHONE }
            )
            ContactStep.PHONE -> Step2WhatsappInput(
                whatsappValue = whatsapp,
                onWhatsappChange = { whatsapp = it },
                onConfirm = { currentStep = ContactStep.EMAIL }
            )
            ContactStep.EMAIL -> Step3EmailInput(
                emailValue = email,
                onEmailChange = { email = it },
                onConfirm = { currentStep = ContactStep.PROCESS_ASK }
            )
            ContactStep.PROCESS_ASK -> Step4ProcessAsk(
                onConfirm = { currentStep = ContactStep.PROCESS_INPUT },
                onDeny = {
                    contactRepository.saveContactInfo(
                        name = nome,
                        whatsapp = whatsapp,
                        email = email,
                        process = null // Usuário escolheu "Não"
                    )
                    onNavigateToHome()
                    currentStep = ContactStep.NAME // Reseta para o início
                }
            )
            ContactStep.PROCESS_INPUT -> Step5ProcessInput(
                numeroProcessoValue = numeroProcesso,
                onNumeroProcessoChange = { numeroProcesso = it },
                onConfirm = {
                    contactRepository.saveContactInfo(
                        name = nome,
                        whatsapp = whatsapp,
                        email = email,
                        process = numeroProcesso // Salva o número do processo
                    )
                    onNavigateToHome()
                    currentStep = ContactStep.NAME // Reseta para o início
                }
            )
        }
    }
}