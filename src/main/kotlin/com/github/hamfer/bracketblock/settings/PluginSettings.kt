package com.github.hamfer.bracketblock.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.ui.JBColor
import java.awt.Color

@State(
    name = "BracketBlockSetting",
    storages = [Storage("SdkSettingsPlugin.xml")]
)
class PluginSettings private constructor() : PersistentStateComponent<PluginSettings.State> {
    companion object {
        private val instance = PluginSettings()

        fun getInstance(): PluginSettings {
            return instance
        }
    }

    class State {
        var borderColor : Color = Color.WHITE
    }

    private var state = State()

    override fun getState(): State {
        return this.state
    }

    override fun loadState(state: State) {
        this.state = state
    }
}