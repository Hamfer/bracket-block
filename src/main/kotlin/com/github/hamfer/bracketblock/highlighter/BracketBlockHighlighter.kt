package com.github.hamfer.bracketblock.highlighter

import com.github.hamfer.bracketblock.adapter.BraceMatchingUtilAdapter
import com.github.hamfer.bracketblock.brace.BracePair
import com.github.hamfer.bracketblock.brace.BraceTokenTypes
import com.github.hamfer.bracketblock.settings.PluginSettings
import com.intellij.lang.Language
import com.intellij.lang.LanguageBraceMatching
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.editor.markup.*
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import java.awt.Color
import java.awt.Font
import java.util.*


class BracketBlockHighlighter(private val editor: Editor) {
    private var languageBracePairs: HashMap<String, List<Pair<IElementType, IElementType>>> = HashMap()
    private val NON_OFFSET = -1
    private val HIGHLIGHT_LAYER_WEIGHT = 100
    private val EMPTY_BRACE_PAIR: BracePair? = null

    private var psiFile: PsiFile?

    init {
        val languageList = Language.getRegisteredLanguages()
        psiFile = editor.project?.let { PsiDocumentManager.getInstance(it).getPsiFile(editor.document) }
        for (language in languageList) {
            val pairedBraceMatcher = LanguageBraceMatching.INSTANCE.forLanguage(language)
            if (pairedBraceMatcher != null) {
                val bracePairs: Array<out com.intellij.lang.BracePair> = pairedBraceMatcher.pairs
                val braceList: MutableList<Pair<IElementType, IElementType>> = LinkedList()
                for (bracePair in bracePairs) {
                    val braceEntry: Pair<IElementType, IElementType> = Pair(
                        bracePair.leftBraceType, bracePair.rightBraceType
                    )
                    braceList.add(braceEntry)
                }
                languageBracePairs[language.id] = braceList
            }
        }
    }

    private fun findClosetBracePairInBraceTokens(offset: Int): BracePair? {
        val editorHighlighter = (editor as EditorEx).highlighter
        val isBlockCaret = this.isBlockCaret()
        val braceTokens: List<Pair<IElementType, IElementType>>? = getSupportedBraceToken()
        if (braceTokens != null) {
            for (braceTokenPair in braceTokens) {
                val leftTraverseIterator = editorHighlighter.createIterator(offset)
                val rightTraverseIterator = editorHighlighter.createIterator(offset)
                val leftBraceOffset: Int = BraceMatchingUtilAdapter.findLeftLParen(
                    leftTraverseIterator,
                    braceTokenPair.first,
                    editor.document.immutableCharSequence,
                    psiFile?.fileType,
                    isBlockCaret
                )
                val rightBraceOffset: Int = BraceMatchingUtilAdapter.findRightRParen(
                    rightTraverseIterator,
                    braceTokenPair.second,
                    editor.document.immutableCharSequence,
                    psiFile?.fileType,
                    isBlockCaret
                )
                if (leftBraceOffset != NON_OFFSET && rightBraceOffset != NON_OFFSET) {
                    return BracePair.BracePairBuilder().leftType(braceTokenPair.first).rightType(braceTokenPair.second)
                        .leftOffset(leftBraceOffset).rightOffset(rightBraceOffset).build()
                }
            }
        }
        return EMPTY_BRACE_PAIR
    }

    private fun findClosetBracePairInStringSymbols(offset: Int): BracePair? {
        if (offset < 0 || editor.document.immutableCharSequence.isEmpty()) return EMPTY_BRACE_PAIR
        val editorHighlighter = (editor as EditorEx).highlighter
        val iterator = editorHighlighter.createIterator(offset)
        val type: IElementType = iterator.tokenType
        val isBlockCaret: Boolean = this.isBlockCaret()
        if (!BraceMatchingUtilAdapter.isStringToken(type)) return EMPTY_BRACE_PAIR
        val leftOffset = iterator.start
        val rightOffset = iterator.end
        return if (!isBlockCaret && leftOffset == offset) EMPTY_BRACE_PAIR else BracePair.BracePairBuilder()
            .leftType(BraceTokenTypes.DOUBLE_QUOTE).rightType(BraceTokenTypes.DOUBLE_QUOTE).leftOffset(leftOffset)
            .rightOffset(rightOffset).build()
    }

    fun findClosetBracePair(offset: Int): BracePair? {
        val braceTokenBracePair: BracePair? = this.findClosetBracePairInBraceTokens(offset)
        val stringSymbolBracePair: BracePair? = this.findClosetBracePairInStringSymbols(offset)
        return if (braceTokenBracePair != null && stringSymbolBracePair != null) {
            if (offset - braceTokenBracePair.leftBrace.offset > offset - stringSymbolBracePair.leftBrace.offset && offset - braceTokenBracePair.rightBrace.offset < offset - stringSymbolBracePair.rightBrace.offset) {
                stringSymbolBracePair
            } else {
                braceTokenBracePair
            }
        } else {
            Optional.ofNullable(braceTokenBracePair).orElse(stringSymbolBracePair)
        }
    }

    fun highlightBracketBlock(bracePair: BracePair?): RangeHighlighter? {
        return if (bracePair != null) {
            val state = PluginSettings.getInstance().state
            println(state.borderColor.toString())
            val textAttribute = TextAttributes(null, null, state.borderColor, EffectType.ROUNDED_BOX, Font.PLAIN)
            editor.markupModel.addRangeHighlighter(
                bracePair.leftBrace.offset,
                bracePair.rightBrace.offset,
                HighlighterLayer.SELECTION + HIGHLIGHT_LAYER_WEIGHT,
                textAttribute,
                HighlighterTargetArea.EXACT_RANGE
            )
        } else {
            null
        }
    }

    fun clearHighlight(highlighterList: List<RangeHighlighter>) {
        highlighterList.forEach { editor.markupModel.removeHighlighter(it) }
    }

    private fun getSupportedBraceToken(): List<Pair<IElementType, IElementType>>? {
        return languageBracePairs[psiFile?.language?.id]
    }

    private fun isBlockCaret(): Boolean {
        return editor.settings.isBlockCursor
    }
}