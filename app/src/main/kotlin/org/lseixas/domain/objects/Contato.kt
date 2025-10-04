package org.lseixas.domain.objects

/*
 * Classe para armazenar informações de contato do usuário
 */
data class Contato(
    val nome: String,
    val whatsapp: String,
    val email: String = "",
    val numeroProcesso: String = ""
)