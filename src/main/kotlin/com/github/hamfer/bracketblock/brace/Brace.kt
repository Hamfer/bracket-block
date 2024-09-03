package com.github.hamfer.bracketblock.brace

import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.openapi.util.TextRange
import com.intellij.psi.tree.IElementType

class Brace {
    private val elementType: IElementType
    val offset: Int
    private val text: String

    constructor(elementType: IElementType, iterator: HighlighterIterator) {
        this.elementType = elementType
        offset = iterator.start
        val document = iterator.document
        text = document.getText(
            TextRange(
                iterator.start,
                iterator.end
            )
        )
    }

    constructor(elementType: IElementType, text: String, offset: Int) {
        this.elementType = elementType
        this.offset = offset
        this.text = text
    }
}
