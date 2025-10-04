package org.lseixas.usecase

import java.time.LocalDate
import org.lseixas.domain.enum.TipoCrime
import org.lseixas.domain.enum.StatusApenado

fun calcularBeneficios(entrada: UsuarioES): UsuarioES {
    
    val penaBaseDias: Int = 
        (UsuarioES.penaAnos * 365) + 
        (UsuarioES.penaMeses * 30) + 
        (UsuarioES.penaDias) - /* As tres primeiras linhas representam a pena total em dias */
        (UsuarioES.detracaoDias) /* Para o calculo deve-se subtrair a detração */

}

fun calcularDataProgressaoSemiaberto(penaBaseDias: Int, entrada: UsuarioES): Int {

    val fracaoProgressao: Float = when (entrada.TipoCrime) {
        COMUM -> when (entrada.StatusApenado) {
            PRIMARIO -> 0.16f
            REINCIDENTE -> 0.2f
        }
        VIOLENCIA_AMEACA -> when (entrada.StatusApenado) {
            PRIMARIO -> 0.25f
            REINCIDENTE -> 0.3f
        }
        HEDIONDO_EQUIPARADO -> when (entrada.StatusApenado) {
            PRIMARIO -> when (entrada.ehHediondoComMorte) {
                false -> 0.4f
                true -> 0.5f
            }
            REINCIDENTE -> when(entrada.ehHediondoComMorte) {
                false -> 0.6f
                true -> 0.7f
            }
        }
    }

    val diasProgredir = (penaBaseDias * fracaoProgressao).toInt()

    return entrada.dataInicioPena.plusDays(diasProgredir.toLong())

}