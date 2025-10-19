package org.lseixas.domain.objects

import org.lseixas.domain.enum.TipoCrime
import org.lseixas.domain.enum.StatusApenado
// NÃO HÁ MAIS IMPORT DE DATETIME AQUI

/**
 * Data class "simples" que transporta todas as informações em tipos primitivos.
 * Ela não tem mais a responsabilidade de lidar com objetos de data.
 */
data class UsuarioES(
    // --- DADOS DE ENTRADA (somente tipos primitivos) ---
    val penaAnos: Int,
    val penaMeses: Int,
    val penaDias: Int,
    val dataInicioPena: String, // <- MUDOU DE LocalDate PARA String
    val detracaoDias: Int = 0,
    val tipoCrime: TipoCrime,
    val statusApenado: StatusApenado,
    val ehHediondoComMorte: Boolean = false,

    // --- DADOS DE SAÍDA (somente tipos primitivos) ---
    val dataProgressaoSemiaberto: String? = null, // <- MUDOU DE LocalDate? PARA String?
    val dataProgressaoAberto: String? = null, // <- MUDOU DE LocalDate? PARA String?
    val dataLivramentoCondicional: String? = null, // <- MUDOU DE LocalDate? PARA String?
    val erro: String? = null
)