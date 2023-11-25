package com.github.hamfer.bracketblock.listeners

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener

class BracketBlockEditorFactoryListener : EditorFactoryListener {
    val bracketBlockEditorCaretListenerMap = HashMap<Editor, BracketBlockCaretListener>();

    override fun editorCreated(event: EditorFactoryEvent) {
        bracketBlockEditorCaretListenerMap[event.editor] = BracketBlockCaretListener(event.editor)
    }

    override fun editorReleased(event: EditorFactoryEvent) {
        bracketBlockEditorCaretListenerMap.remove(event.editor)?.dispose()
    }
}