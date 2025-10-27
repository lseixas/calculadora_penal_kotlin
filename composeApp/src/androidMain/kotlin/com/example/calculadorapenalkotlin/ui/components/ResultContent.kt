package com.example.calculadorapenalkotlin.ui.components // Pacote correto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.lseixas.domain.objects.UsuarioES // Importa a classe de dados do módulo shared

/**
 * Um Composable reutilizável para exibir o conteúdo dos resultados do cálculo penal.
 * Usado tanto no AlertDialog quanto no Card inferior.
 *
 * @param result O objeto UsuarioES contendo os resultados calculados (pode ser nulo).
 */
@Composable
fun ResultContent(result: UsuarioES?) {
    Column(
        // Padding interno para espaçamento dentro do Card/Dialog
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.Start // Alinha o texto à esquerda por padrão
    ) {
        Text(
            text = "Semiaberto: ${result?.dataProgressaoSemiaberto ?: "N/A"}",
            modifier = Modifier.fillMaxWidth() // Garante que o texto ocupe a linha
        )
        Spacer(modifier = Modifier.height(4.dp)) // Espaçamento entre as linhas
        Text(
            text = "Aberto: ${result?.dataProgressaoAberto ?: "N/A"}",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Livramento: ${result?.dataLivramentoCondicional ?: "Não aplicável"}",
            modifier = Modifier.fillMaxWidth()
        )

        // Exibe a mensagem de erro, se houver
        if (result?.erro != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Erro: ${result.erro}",
                color = MaterialTheme.colorScheme.error, // Usa a cor de erro do tema
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodySmall // Estilo menor para o erro
            )
        }
    }
}