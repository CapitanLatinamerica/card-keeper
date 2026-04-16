package com.supersonic.evercard.features.settings.ui

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.core.content.edit

class SettingsViewModel(
    private val prefs: SharedPreferences
) : ViewModel() {

    // Текущая тема (light, dark, carnival)
    private val _currentTheme = MutableStateFlow("light")
    val currentTheme: StateFlow<String> = _currentTheme.asStateFlow()

    // Текущий режим отображения (list, carousel)
    private val _displayMode = MutableStateFlow("carousel")
    val displayMode: StateFlow<String> = _displayMode.asStateFlow()

    init {
        loadSavedSettings()
    }

    fun setTheme(theme: String) {
        viewModelScope.launch {
            _currentTheme.value = theme
            saveTheme(theme)
            applyTheme(theme)
        }
    }

    fun setDisplayMode(mode: String) {
        viewModelScope.launch {
            _displayMode.value = mode
            saveDisplayMode(mode)
        }
    }

    private fun loadSavedSettings() {
        _currentTheme.value = prefs.getString("app_theme", "light") ?: "light"
        _displayMode.value = prefs.getString("display_mode", "carousel") ?: "carousel"
    }

    private fun saveTheme(theme: String) {
        prefs.edit { putString("app_theme", theme) }
    }

    private fun saveDisplayMode(mode: String) {
        prefs.edit { putString("display_mode", mode) }
    }

    private fun applyTheme(theme: String) {
        val mode = when (theme) {
            "light" -> AppCompatDelegate.MODE_NIGHT_NO
            "dark", "carnival" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}