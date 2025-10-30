// Caminho do arquivo: composeApp/src/commonMain/kotlin/com/example/calculadorapenalkotlin/App.kt

package com.example.calculadorapenalkotlin // Certifique-se que o package está correto

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.calculadorapenalkotlin.navigation.Screen // Importando o enum da nova pasta 'navigation' (anterior MainScreenEnum)
import com.example.calculadorapenalkotlin.ui.screens.contact.ContactFormScreen // Importando a tela da nova pasta (anterior MultiStepScreen)
import com.example.calculadorapenalkotlin.ui.screens.home.HomePage // Importando a tela da nova pasta
import com.example.calculadorapenalkotlin.ui.theme.CalculadoraPenalKotlinTheme // Importando o tema

@Composable
fun App() {
    CalculadoraPenalKotlinTheme {

        val dependencies = rememberAppDependencies()

        val hasContactInfo = remember { dependencies.contactRepository.hasCompleteContactInfo() }

        var currentScreen by remember {
            mutableStateOf(
                if (hasContactInfo) Screen.HOME else Screen.CONTACT_FORM
            )
        }

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            when (currentScreen) {
                Screen.HOME -> HomePage(
                    onNavigateToContactForm = { currentScreen = Screen.CONTACT_FORM }
                )
                Screen.CONTACT_FORM -> ContactFormScreen(
                    contactRepository = dependencies.contactRepository,
                    onNavigateToHome = { currentScreen = Screen.HOME }
                )
            }
        }
    }
}