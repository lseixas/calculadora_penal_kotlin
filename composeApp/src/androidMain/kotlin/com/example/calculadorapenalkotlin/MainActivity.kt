package com.example.calculadorapenalkotlin // Pacote raiz do app Android

// Imports necessários
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.calculadorapenalkotlin.ui.theme.CalculadoraPenalKotlinTheme // Importa o tema (que está no commonMain)

/**
 * A Activity principal do aplicativo Android.
 * Sua única responsabilidade é configurar o tema e chamar
 * a função Composable raiz 'App()' que está definida no commonMain,
 * onde toda a lógica de UI compartilhada reside.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}