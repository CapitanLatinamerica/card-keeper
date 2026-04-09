package com.supersonic.evercard.features.root.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.supersonic.evercard.R

class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
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
}