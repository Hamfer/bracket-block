package com.github.hamfer.bracketblock.settings

import com.intellij.ui.ColorPanel
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import org.jetbrains.annotations.NotNull
import java.awt.Color
import javax.swing.JComponent
import javax.swing.JPanel


class PluginSettingsComponent {
    private var mainPanel: JPanel? = null
    private val borderColor = ColorPanel()

    init {
        val state = PluginSettings.getInstance().state
        borderColor.selectedColor = state.borderColor
        mainPanel = FormBuilder.createFormBuilder().addLabeledComponent(JBLabel("Border color:"), borderColor, 1, false)
            .addComponentFillVertically(JPanel(), 0).panel
    }

    fun getPanel(): JPanel? {
        return mainPanel
    }

    fun getPreferredFocusedComponent(): JComponent {
        return borderColor
    }

    @NotNull
    fun getBorderColor(): Color {
        return borderColor.selectedColor ?: JBColor.WHITE
    }

    fun setBorderColor(color: Color) {
        borderColor.selectedColor = color
    }
}