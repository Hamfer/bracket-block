package com.github.hamfer.bracketblock.settings

import com.intellij.ui.ColorPanel
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import org.jetbrains.annotations.NotNull
import java.awt.Color
import javax.swing.JPanel


class PluginSettingsComponent {
    private var mainPanel: JPanel? = null
    private val borderColor = ColorPanel()

    init {
        val instance = PluginSettings.getInstance()
        borderColor.selectedColor = instance.getBorderColor()
        mainPanel = FormBuilder.createFormBuilder().addLabeledComponent(JBLabel("Border color:"), borderColor, 1, false)
            .addComponentFillVertically(JPanel(), 0).panel
    }

    fun getPanel(): JPanel? {
        return mainPanel
    }

    @NotNull
    fun getBorderColor(): Color {
        return borderColor.selectedColor ?: JBColor.WHITE
    }
}