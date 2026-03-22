package com.supersonic.evercard.features.cards_list.ui

import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supersonic.evercard.features.root.data.DiscountCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CardListViewModel : ViewModel() {

    // Состояние списка карт
    private val _cards = MutableStateFlow<List<DiscountCard>>(emptyList())
    val cards: StateFlow<List<DiscountCard>> = _cards.asStateFlow()

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
                _cards.value = listOf(
                    DiscountCard("1", "Пятёрочка", Color.parseColor("#E31E24"), null, "123456"),
                    DiscountCard("2", "Перекрёсток", Color.parseColor("#00704A"), null, "789012"),
                    DiscountCard("3", "Магнит", Color.parseColor("#FF0000"), null, "345678"),
                    DiscountCard("4", "Лента", Color.parseColor("#0039A6"), null, "901234")
                )
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}