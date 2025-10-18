package org.lseixas.domain.objects

import org.lseixas.domain.enum.TipoCrime
import org.lseixas.domain.enum.StatusApenado
import java.time.LocalDate

/**
 * Data class que transporta todas as informações de entrada (input) e saída (output)
 * do usuário e dos cálculos.
 */
data class UsuarioES(
    // --- DADOS DE ENTRADA (INPUTS) ---
    val penaAnos: Int,
    val penaMeses: Int,
    val penaDias: Int,
    val dataInicioPena: LocalDate,
    val detracaoDias: Int = 0, // Pode ter um valor padrão de 0
    val tipoCrime: TipoCrime, // Usar um Enum é uma ótima prática
    val statusApenado: StatusApenado, // Outro Enum aqui
    val ehHediondoComMorte: Boolean = false, // Um campo importante que notei nas regras

    // --- DADOS DE SAÍDA (OUTPUTS) ---
    val dataProgressaoSemiaberto: LocalDate? = null,
    val dataProgressaoAberto: LocalDate? = null,
    val dataLivramentoCondicional: LocalDate? = null,
    val erro: String? = null // Um campo para mensagens de erro, se o cálculo falhar
)