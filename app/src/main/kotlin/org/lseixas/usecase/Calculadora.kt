package org.lseixas.usecase

import org.lseixas.domain.enum.TipoCrime
import org.lseixas.domain.enum.StatusApenado
import org.lseixas.domain.objects.UsuarioES
import java.time.LocalDate


data class ResultadoProgressao(
    val diasParaProgredir: Int,
    val fracaoUtilizada: Float
)

fun calcularBeneficios(entrada: UsuarioES): UsuarioES {


    // Semiaberto e Aberto
    val penaBaseDias: Int = (entrada.penaAnos * 365) + (entrada.penaMeses * 30) + (entrada.penaDias) - (entrada.detracaoDias)
    val resultadoSemiAberto: ResultadoProgressao = calcularDataProgressaoSemiaberto(penaBaseDias = penaBaseDias, entrada = entrada)
    val dataDoSemiAberto: LocalDate = entrada.dataInicioPena.plusDays(resultadoSemiAberto.diasParaProgredir.toLong())
    val penaRestanteEmDias = penaBaseDias - resultadoSemiAberto.diasParaProgredir
    val diasAdicionaisParaOAberto = (penaRestanteEmDias * resultadoSemiAberto.fracaoUtilizada).toInt()
    val dataDoAberto: LocalDate = dataDoSemiAberto.plusDays(diasAdicionaisParaOAberto.toLong())


    // Livramento
    val diasParaLivramento: Int = calcularDataLivramento(
        penaBaseDias = penaBaseDias,
        entrada = entrada
    )
    val dataLivramentoFinal: LocalDate?
    if (diasParaLivramento > 0) {
        dataLivramentoFinal = entrada.dataInicioPena.plusDays(diasParaLivramento.toLong())
    } else {
        dataLivramentoFinal = null
    }


    return entrada.copy(
        dataProgressaoSemiaberto = dataDoSemiAberto,
        dataProgressaoAberto = dataDoAberto,
        dataLivramentoCondicional = dataLivramentoFinal
    )
}

fun calcularDataLivramento(penaBaseDias: Int, entrada: UsuarioES): Int {

    val penaResultado: Int = when (entrada.tipoCrime) {
        TipoCrime.HEDIONDO_EQUIPARADO -> when (entrada.ehHediondoComMorte) {
            true -> 0
            false -> (penaBaseDias * (2.0 / 3.0)).toInt()
        }
        else -> when (entrada.statusApenado) {
            StatusApenado.REINCIDENTE -> (penaBaseDias * 0.5).toInt()
            StatusApenado.PRIMARIO -> (penaBaseDias * (1.0 / 3.0)).toInt()
        }
    }

    return penaResultado
}

fun calcularDataProgressaoSemiaberto(penaBaseDias: Int, entrada: UsuarioES): ResultadoProgressao {

    val fracaoProgressao: Float = when (entrada.tipoCrime) {
        TipoCrime.COMUM -> when (entrada.statusApenado) {
            StatusApenado.PRIMARIO -> 0.16f
            StatusApenado.REINCIDENTE -> 0.20f
        }
        TipoCrime.VIOLENCIA_AMEACA -> when (entrada.statusApenado) {
            StatusApenado.PRIMARIO -> 0.25f
            StatusApenado.REINCIDENTE -> 0.30f
        }
        TipoCrime.HEDIONDO_EQUIPARADO -> when (entrada.statusApenado) {
            StatusApenado.PRIMARIO -> when (entrada.ehHediondoComMorte) {
                false -> 0.40f
                true -> 0.50f
            }
            StatusApenado.REINCIDENTE -> when (entrada.ehHediondoComMorte) {
                false -> 0.60f
                true -> 0.70f
            }
        }
    }

    val diasParaProgredir = (penaBaseDias * fracaoProgressao).toInt()

    return ResultadoProgressao(
        diasParaProgredir = diasParaProgredir,
        fracaoUtilizada = fracaoProgressao
    )

}