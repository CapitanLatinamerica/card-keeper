package com.supersonic.evercard.features.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashFragmentViewModel : ViewModel() {

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            val minDisplayTime = 300L // Минимальное время показа сплэша

            // TODO: Здесь будет загрузка из БД
            // Пока просто имитируем загрузку
            delay(500) // Симулируем загрузку данных

            val elapsedTime = System.currentTimeMillis() - startTime
            val remainingTime = (minDisplayTime - elapsedTime).coerceAtLeast(0)

            delay(remainingTime)
            _isReady.value = true
            _isLoading.value = false
        }
    }
}