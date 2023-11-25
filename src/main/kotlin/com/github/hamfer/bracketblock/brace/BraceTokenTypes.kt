package com.github.hamfer.bracketblock.brace

import com.intellij.lang.Language
import com.intellij.psi.tree.IElementType


object BraceTokenTypes {
    private val ElementType2Text: MutableMap<IElementType, String> = HashMap()
    val DOUBLE_QUOTE = IElementType("DOUBLE_QUOTE", Language.ANY)
    const val GROOVY_STRING_TOKEN = "Gstring"
    const val GROOVY_SINGLE_QUOTE_TOKEN = "string"
    const val KOTLIN_STRING_TOKEN = "REGULAR_STRING_PART"
    const val KOTLIN_CHAR_TOKEN = "CHARACTER_LITERAL"
    const val JS_STRING_TOKEN = "STRING"
    const val JAVA_STRING_TOKEN = "STRING_LITERAL"
    const val SCALA_STRING_TOKEN = "string content"
    const val HASKELL_STRING_TOKEN = "HaskellTokenType.STRING_LITERAL"

    init {
        ElementType2Text[DOUBLE_QUOTE] = "\""
    }

    fun getElementTypeText(type: IElementType): String? {
        return ElementType2Text[type]
    }
}

