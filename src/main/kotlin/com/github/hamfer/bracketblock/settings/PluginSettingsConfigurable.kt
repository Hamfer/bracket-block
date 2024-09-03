package com.github.hamfer.bracketblock.settings

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class PluginSettingsConfigurable : Configurable {
    private var pluginSettingsComponent: PluginSettingsComponent? = null

    override fun createComponent(): JComponent? {
        pluginSettingsComponent = PluginSettingsComponent()
        return pluginSettingsComponent!!.getPanel()
    }

    override fun isModified(): Boolean {
        val state = PluginSettings.getInstance().state
        return pluginSettingsComponent!!.getBorderColor() != state.borderColor
    }

    override fun apply() {
        val state = PluginSettings.getInstance().state
        state.borderColor = pluginSettingsComponent!!.getBorderColor()
    }

    override fun getDisplayName(): String {
        return "Bracket Block"
    }
}