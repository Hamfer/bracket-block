package com.github.hamfer.bracketblock.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import java.awt.Color

@Service
@State(
    name = "BracketBlockSetting", storages = [Storage("bracket-block.xml")]

)
class PluginSettings private constructor() : PersistentStateComponent<PluginSettingsState> {
    companion object {
        fun getInstance(): PluginSettings {
            return ApplicationManager.getApplication().getService(PluginSettings::class.java)
        }
    }

    private var state = PluginSettingsState()

    override fun getState(): PluginSettingsState {
        return this.state
    }

    override fun loadState(state: PluginSettingsState) {
        this.state = state
    }

    fun setBorderColor(color: Color) {
        setBorderColor(color.rgb)
    }

    fun setBorderColor(color: Int) {
        getState().borderColor = color
    }

    fun getBorderColor(): Color {
        return Color(
            (getState().borderColor shr 16) and 0xFF,
            (getState().borderColor shr 8) and 0xFF,
            getState().borderColor and 0xFF
        )
    }
}