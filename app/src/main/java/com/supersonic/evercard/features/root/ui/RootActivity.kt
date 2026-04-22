package com.supersonic.evercard.features.root.ui

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.supersonic.evercard.R
import com.supersonic.evercard.databinding.ActivityRootBinding
import org.koin.android.ext.android.get

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        applySavedTheme()
        val prefs = get<SharedPreferences>()
        val savedTheme = prefs.getString("app_theme", "light") ?: "light"

        when (savedTheme) {
            "carnival" -> setTheme(R.style.Theme_Evercard_Carnival)
            // "light" и "dark" не требуют setTheme, они управляются через AppCompatDelegate
            else -> { /* стандартная тема из манифеста */ }
        }
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Проверяем, изменился ли ночной режим
        val currentNightMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val oldNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (currentNightMode != oldNightMode) {
            // Пересоздаём Activity
            recreate()
        }
    }

    private fun applySavedTheme() {
        val prefs = get<android.content.SharedPreferences>()
        val savedTheme = prefs.getString("app_theme", "light") ?: "light"

        val mode = when (savedTheme) {
            "light" -> AppCompatDelegate.MODE_NIGHT_NO
            "dark", "carnival" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}