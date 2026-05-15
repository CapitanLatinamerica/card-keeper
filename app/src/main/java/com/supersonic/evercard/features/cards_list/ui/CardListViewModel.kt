package com.supersonic.evercard.features.cards_list.ui

import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supersonic.evercard.features.root.data.DiscountCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CardListViewModel : ViewModel() {

    private val _allCards = MutableStateFlow<List<DiscountCard>>(emptyList())
    val allCards: StateFlow<List<DiscountCard>> = _allCards.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadCards() // Загружаем данные при создании ViewModel
    }


    private fun loadCards() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Позже здесь будет запрос к репозиторию
                // Сейчас имитируем задержку
                delay(1000)
                _allCards.value = listOf(
                    DiscountCard("1", "Pyaterochka", Color.parseColor("#E31E24"), null, "123456"),
                    DiscountCard("2", "Perekrestok", Color.parseColor("#00704A"), null, "789012"),
                    DiscountCard("3", "Magnit", Color.parseColor("#FF0000"), null, "345678"),
                    DiscountCard("4", "Lenta", Color.parseColor("#0039A6"), null, "901234")
                )
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}