package com.supersonic.evercard.features.cards_list.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supersonic.evercard.features.root.data.DiscountCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CardListViewModel : ViewModel() {

    // Состояние списка карт
    private val _cards = MutableStateFlow<List<DiscountCard>>(emptyList())
    val cards: StateFlow<List<DiscountCard>> = _cards.asStateFlow()

    init {
        loadCards() // Загружаем данные при создании ViewModel
    }

    private fun loadCards() {
        viewModelScope.launch {
            // Позже здесь будет запрос к репозиторию
            // Сейчас пока фейковые данные
            _cards.value = listOf(
                DiscountCard("1", "Пятёрочка", android.graphics.Color.parseColor("#E31E24"), null, "123456"),
                DiscountCard("2", "Перекрёсток", android.graphics.Color.parseColor("#00704A"), null, "789012"),
                DiscountCard("3", "Магнит", android.graphics.Color.parseColor("#FF0000"), null, "345678"),
                DiscountCard("4", "Лента", android.graphics.Color.parseColor("#0039A6"), null, "901234")
            )
        }
    }
}