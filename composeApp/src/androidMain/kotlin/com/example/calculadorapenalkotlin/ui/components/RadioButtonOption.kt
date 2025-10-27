package com.example.calculadorapenalkotlin.ui.components // Pacote correto

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Um Composable reutilizável que exibe um RadioButton com um texto ao lado.
 * Projetado para ser usado dentro de uma RowScope (geralmente dentro de uma Row).
 * O modificador weight(1f) garante que as opções dividam o espaço igualmente.
 *
 * @param text O texto a ser exibido ao lado do RadioButton.
 * @param selected Se esta opção está atualmente selecionada.
 * @param onClick A ação a ser executada quando esta opção for clicada.
 */
@Composable
fun RowScope.RadioButtonOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .weight(1f) // Ocupa espaço igual dentro da Row pai
            .padding(vertical = 8.dp), // Espaçamento vertical
        verticalAlignment = Alignment.CenterVertically // Alinha o botão e o texto
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 4.dp) // Espaço entre o botão e o texto
        )
    }
}