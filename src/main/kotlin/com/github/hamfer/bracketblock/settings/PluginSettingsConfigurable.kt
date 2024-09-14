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
        val instance = PluginSettings.getInstance()
        return pluginSettingsComponent!!.getBorderColor() != instance.getBorderColor()
    }

    override fun apply() {
        val instance = PluginSettings.getInstance()
        instance.setBorderColor(pluginSettingsComponent!!.getBorderColor())
    }

    override fun getDisplayName(): String {
        return "Bracket Block"
    }
}