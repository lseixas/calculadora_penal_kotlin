package org.lseixas.usecase

import kotlinx.datetime.DateTimeUnit
import org.lseixas.domain.enum.TipoCrime
import org.lseixas.domain.enum.StatusApenado
import org.lseixas.domain.objects.UsuarioES
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus


data class ResultadoProgressao(
    val diasParaProgredir: Int,
    val fracaoUtilizada: Float
)

fun calcularBeneficios(entrada: UsuarioES): UsuarioES {

    // --- ETAPA 1: CONVERTER E VALIDAR AS ENTRADAS ---
    val dataInicioObj: LocalDate
    try {
        val dateParts = entrada.dataInicioPena.split("/")
        dataInicioObj = LocalDate(
            year = dateParts[2].toInt(),
            monthNumber = dateParts[1].toInt(),
            dayOfMonth = dateParts[0].toInt()
        )
    } catch (e: Exception) {
        // Se a conversão falhar, retorna o objeto original com uma mensagem de erro.
        return entrada.copy(erro = "Formato de data inválido. Use DD/MM/AAAA.")
    }

    // --- ETAPA 2: EXECUTAR A LÓGICA DE CÁLCULO (código existente) ---
    // Esta parte do código não muda, mas agora usa 'dataInicioObj' que criamos.
    val penaBaseDias: Int = (entrada.penaAnos * 365) + (entrada.penaMeses * 30) + (entrada.penaDias) - (entrada.detracaoDias)
    val resultadoSemiAberto: ResultadoProgressao = calcularDataProgressaoSemiaberto(penaBaseDias = penaBaseDias, entrada = entrada)
    val dataDoSemiAberto: LocalDate = dataInicioObj.plus(resultadoSemiAberto.diasParaProgredir, DateTimeUnit.DAY)
    val penaRestanteEmDias = penaBaseDias - resultadoSemiAberto.diasParaProgredir
    val diasAdicionaisParaOAberto = (penaRestanteEmDias * resultadoSemiAberto.fracaoUtilizada).toInt()
    val dataDoAberto: LocalDate = dataDoSemiAberto.plus(diasAdicionaisParaOAberto, DateTimeUnit.DAY)

    val diasParaLivramento: Int = calcularDataLivramento(penaBaseDias = penaBaseDias, entrada = entrada)
    val dataLivramentoFinal: LocalDate? = if (diasParaLivramento > 0) {
        dataInicioObj.plus(diasParaLivramento, DateTimeUnit.DAY)
    } else {
        null
    }

    // --- ETAPA 3: CONVERTER OS RESULTADOS DE VOLTA PARA STRING ---
    // Helper function para formatar a data de forma consistente
    fun formatarData(data: LocalDate?): String? {
        return data?.let {
            // padStart garante que o formato seja sempre "01/09/2025" e não "1/9/2025"
            val dia = it.dayOfMonth.toString().padStart(2, '0')
            val mes = it.monthNumber.toString().padStart(2, '0')
            "${dia}/${mes}/${it.year}"
        }
    }

    return entrada.copy(
        dataProgressaoSemiaberto = formatarData(dataDoSemiAberto),
        dataProgressaoAberto = formatarData(dataDoAberto),
        dataLivramentoCondicional = formatarData(dataLivramentoFinal)
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