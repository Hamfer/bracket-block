package com.github.hamfer.bracketblock.adapter

import com.github.hamfer.bracketblock.brace.BraceTokenTypes.GROOVY_SINGLE_QUOTE_TOKEN
import com.github.hamfer.bracketblock.brace.BraceTokenTypes.GROOVY_STRING_TOKEN
import com.github.hamfer.bracketblock.brace.BraceTokenTypes.HASKELL_STRING_TOKEN
import com.github.hamfer.bracketblock.brace.BraceTokenTypes.JAVA_STRING_TOKEN
import com.github.hamfer.bracketblock.brace.BraceTokenTypes.JS_STRING_TOKEN
import com.github.hamfer.bracketblock.brace.BraceTokenTypes.KOTLIN_CHAR_TOKEN
import com.github.hamfer.bracketblock.brace.BraceTokenTypes.KOTLIN_STRING_TOKEN
import com.github.hamfer.bracketblock.brace.BraceTokenTypes.SCALA_STRING_TOKEN
import com.intellij.codeInsight.highlighting.BraceMatchingUtil.*
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.tree.IElementType
import java.util.*
import kotlin.collections.HashSet


object BraceMatchingUtilAdapter {
    private val STRING_TOKEN_SET: MutableSet<String> = HashSet()

    init {
        STRING_TOKEN_SET.add(GROOVY_STRING_TOKEN)
        STRING_TOKEN_SET.add(GROOVY_SINGLE_QUOTE_TOKEN)
        STRING_TOKEN_SET.add(KOTLIN_STRING_TOKEN)
        STRING_TOKEN_SET.add(KOTLIN_CHAR_TOKEN)
        STRING_TOKEN_SET.add(JS_STRING_TOKEN)
        STRING_TOKEN_SET.add(JAVA_STRING_TOKEN)
        STRING_TOKEN_SET.add(SCALA_STRING_TOKEN)
        STRING_TOKEN_SET.add(HASKELL_STRING_TOKEN)
    }

    /**
     * check is the current token type is string token.
     * @param tokenType token type
     * @return is string token
     */
    fun isStringToken(tokenType: IElementType): Boolean {
        val elementName: String = tokenType.toString()
        return STRING_TOKEN_SET.contains(elementName)
    }

    /**
     * Find the left closest brace offset position.
     *
     * @param iterator highlighter iterator
     * @param lparenTokenType left token type to be paired
     * @param fileText file text
     * @param fileType file type
     * @return offset
     */
    fun findLeftLParen(
        iterator: HighlighterIterator,
        lparenTokenType: IElementType,
        fileText: CharSequence?,
        fileType: FileType?, isBlockCaret: Boolean
    ): Int {
        val lastLbraceOffset = -1
        val initOffset = if (iterator.atEnd()) -1 else iterator.start
        val braceStack: Stack<IElementType> = Stack()
        while (!iterator.atEnd()) {
            val tokenType: IElementType = iterator.tokenType
            if (isLBraceToken(iterator, fileText!!, fileType!!)) {
                if (!isBlockCaret && initOffset == iterator.start) {
                    iterator.retreat()
                    continue
                }
                if (braceStack.isNotEmpty()) {
                    val topToken: IElementType = braceStack.pop()
                    if (!isPairBraces(tokenType, topToken, fileType)) {
                        break // unmatched braces
                    }
                } else {
                    return if (tokenType === lparenTokenType) {
                        iterator.start
                    } else {
                        break
                    }
                }
            } else if (isRBraceToken(iterator, fileText, fileType)) {
                if (initOffset == iterator.start) {
                    iterator.retreat()
                    continue
                }
                braceStack.push(iterator.tokenType)
            }
            iterator.retreat()
        }
        return lastLbraceOffset
    }

    /**
     * find the right closest brace offset position
     *
     * @param iterator highlight iterator
     * @param rparenTokenType right token type to paired
     * @param fileText file text
     * @param fileType file type
     * @return offset
     */
    fun findRightRParen(
        iterator: HighlighterIterator,
        rparenTokenType: IElementType,
        fileText: CharSequence?,
        fileType: FileType?, isBlockCaret: Boolean
    ): Int {
        val lastRbraceOffset = -1
        val initOffset = if (iterator.atEnd()) -1 else iterator.start
        val braceStack: Stack<IElementType> = Stack()
        while (!iterator.atEnd()) {
            val tokenType: IElementType = iterator.tokenType
            if (isRBraceToken(iterator, fileText!!, fileType!!)) {
                if (braceStack.isNotEmpty()) {
                    val topToken: IElementType = braceStack.pop()
                    if (!isPairBraces(tokenType, topToken, fileType)) {
                        break // unmatched braces
                    }
                } else {
                    return if (tokenType === rparenTokenType) {
                        iterator.start + 1
                    } else {
                        break
                    }
                }
            } else if (isLBraceToken(iterator, fileText, fileType)) {
                if (isBlockCaret && initOffset == iterator.start) {
                    iterator.advance()
                    continue
                } else braceStack.push(iterator.tokenType)
            }
            iterator.advance()
        }
        return lastRbraceOffset
    }
}

