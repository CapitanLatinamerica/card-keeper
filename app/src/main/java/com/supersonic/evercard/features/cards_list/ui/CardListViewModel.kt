package com.supersonic.evercard.features.cards_list.ui

import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supersonic.evercard.data.db.repository.LoyaltyCardRepository
import com.supersonic.evercard.domain.model.LoyaltyCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CardListViewModel(
    private val repository: LoyaltyCardRepository
) : ViewModel() {

    private val _allCards = MutableStateFlow<List<LoyaltyCard>>(emptyList())
    val allCards: StateFlow<List<LoyaltyCard>> = _allCards.asStateFlow()

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

            repository.getAllCards()
                .catch { e ->
                    _error.value = "Ошибка загрузки: ${e.message}"
                    _isLoading.value = false
                }
                .collect { cards ->
                    _allCards.value = cards
                    _isLoading.value = false
                }
        }
    }

    fun toggleFavorite(card: LoyaltyCard) {
        viewModelScope.launch {
            val updatedCard = card.copy(isFavorite = !card.isFavorite)
            repository.updateCard(updatedCard)
        }
    }
}