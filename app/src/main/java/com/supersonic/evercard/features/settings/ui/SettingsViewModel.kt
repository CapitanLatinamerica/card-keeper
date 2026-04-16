package com.supersonic.evercard.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    fun setDarkTheme(isDark: Boolean) {
        viewModelScope.launch {
            _isDarkTheme.value = isDark
            // Здесь позже добавим сохранение в SharedPreferences
        }
    }
}