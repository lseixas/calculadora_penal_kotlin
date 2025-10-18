package com.example.calculadorapenalkotlin.transformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // A entrada são apenas os dígitos, ex: "11993313440"
        val trimmed = if (text.text.length >= 11) text.text.substring(0..10) else text.text

        // Formata o texto adicionando hifens na posição correta
        val phoneMask = trimmed.mapIndexed { index, char ->
            when (index) {
                1 -> "$char-" // Adiciona o primeiro hífen após o 2º dígito (DDD)
                6 -> "$char-" // Adiciona o segundo hífen após o 7º dígito
                else -> char
            }
        }.joinToString("")

        // Mapeia os movimentos do cursor
        val phoneOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Posição do cursor no texto com máscara
                if (offset <= 1) return offset
                if (offset <= 6) return offset + 1
                if (offset <= 11) return offset + 2
                return 13 // Comprimento máximo da máscara (11 dígitos + 2 hifens)
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Posição do cursor no texto original (só dígitos)
                if (offset <= 2) return offset
                if (offset <= 8) return offset - 1
                if (offset <= 13) return offset - 2
                return 11 // Comprimento máximo do original
            }
        }

        return TransformedText(AnnotatedString(phoneMask), phoneOffsetTranslator)
    }
}