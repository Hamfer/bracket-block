package com.github.hamfer.bracketblock.brace

import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.psi.tree.IElementType

class BracePair {
    val leftBrace: Brace
    val rightBrace: Brace

    constructor(
        leftType: IElementType?,
        rightType: IElementType?,
        leftText: String?,
        rightText: String?,
        leftOffset: Int,
        rightOffset: Int
    ) {
        leftBrace = Brace(leftType!!, leftText!!, leftOffset)
        rightBrace = Brace(rightType!!, rightText!!, rightOffset)
    }

    constructor(
        leftType: IElementType?,
        rightType: IElementType?,
        leftIterator: HighlighterIterator?,
        rightIterator: HighlighterIterator?
    ) {
        leftBrace = Brace(leftType!!, leftIterator!!)
        rightBrace = Brace(rightType!!, rightIterator!!)
    }

    class BracePairBuilder {
        private var leftType: IElementType? = null
        private var rightType: IElementType? = null
        private var leftOffset = 0
        private var rightOffset = 0
        private var leftIterator: HighlighterIterator? = null
        private var rightIterator: HighlighterIterator? = null
        private var leftText: String? = null
        private var rightText: String? = null
        fun leftType(type: IElementType?): BracePairBuilder {
            leftType = type
            return this
        }

        fun rightType(type: IElementType?): BracePairBuilder {
            rightType = type
            return this
        }

        fun leftIterator(iterator: HighlighterIterator?): BracePairBuilder {
            leftIterator = iterator
            return this
        }

        fun rightIterator(iterator: HighlighterIterator?): BracePairBuilder {
            rightIterator = iterator
            return this
        }

        fun leftOffset(offset: Int): BracePairBuilder {
            leftOffset = offset
            return this
        }

        fun rightOffset(offset: Int): BracePairBuilder {
            rightOffset = offset
            return this
        }

        fun leftText(text: String?): BracePairBuilder {
            leftText = text
            return this
        }

        fun rightText(text: String?): BracePairBuilder {
            rightText = text
            return this
        }

        fun build(): BracePair {
            return if (leftIterator == null) {
                var leftText = if (leftText == null) leftType?.let { BraceTokenTypes.getElementTypeText(it) } else leftText
                var rightText = if (rightText == null) rightType?.let { BraceTokenTypes.getElementTypeText(it) } else rightText
                leftText = leftText ?: ""
                rightText = rightText ?: ""
                BracePair(
                    leftType, rightType,
                    leftText, rightText,
                    leftOffset, rightOffset
                )
            } else { // created by the iterator
                BracePair(
                    leftType, rightType,
                    leftIterator, rightIterator
                )
            }
        }
    }
}

