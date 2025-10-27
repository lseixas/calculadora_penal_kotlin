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
    CalculadoraPenalKotlinTheme { // Aplica o tema geral do app

        // State que controla qual tela principal está sendo exibida
        // Começa na HomePage por padrão
        var currentScreen by remember { mutableStateOf(Screen.CONTACT_FORM) }

        // O Surface é o container raiz da UI
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            // O 'when' decide qual Composable de tela inteira mostrar
            when (currentScreen) {
                Screen.HOME -> HomePage(
                    // Passa uma função lambda que a HomePage pode chamar
                    // para solicitar a navegação para o formulário de contato.
                    onNavigateToContactForm = { currentScreen = Screen.CONTACT_FORM }
                )
                Screen.CONTACT_FORM -> ContactFormScreen( // Usando o nome mais descritivo
                    // Passa uma função lambda que o ContactFormScreen pode chamar
                    // para solicitar a navegação de volta para a HomePage.
                    onNavigateToHome = { currentScreen = Screen.HOME }
                )
            }
        }
    }
}