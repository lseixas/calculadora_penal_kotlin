package com.example.calculadorapenalkotlin.ui.util.transformations // Ou o pacote apropriado

    import androidx.compose.ui.text.AnnotatedString
    import androidx.compose.ui.text.input.OffsetMapping
    import androidx.compose.ui.text.input.TransformedText
    import androidx.compose.ui.text.input.VisualTransformation

    class DMYVisualTransformation : VisualTransformation {
        override fun filter(text: AnnotatedString): TransformedText {
            // A entrada são apenas os dígitos, ex: "27102025" (DDMMAAAA)
            // Limitamos a 8 dígitos
            val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text

            // Formata o texto adicionando barras '/' na posição correta
            val dateMask = trimmed.mapIndexedNotNull { index, char ->
                when (index) {
                    1 -> "$char/" // Adiciona a primeira barra após o 2º dígito (DD)
                    3 -> "$char/" // Adiciona a segunda barra após o 4º dígito (MM)
                    else -> char
                }
            }.joinToString("")

            // Mapeia os movimentos do cursor
            val dateOffsetTranslator = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    // Posição do cursor no texto com máscara (DD/MM/AAAA)
                    if (offset <= 1) return offset // Antes da primeira barra
                    if (offset <= 3) return offset + 1 // Entre as barras
                    if (offset <= 8) return offset + 2 // Após a segunda barra
                    return 10 // Comprimento máximo da máscara (8 dígitos + 2 barras)
                }

                override fun transformedToOriginal(offset: Int): Int {
                    // Posição do cursor no texto original (só dígitos DDMMAAAA)
                    if (offset <= 2) return offset // Antes da primeira barra
                    if (offset <= 5) return offset - 1 // Entre as barras
                    if (offset <= 10) return offset - 2 // Após a segunda barra
                    return 8 // Comprimento máximo do original
                }
            }

            return TransformedText(AnnotatedString(dateMask), dateOffsetTranslator)
        }
    }