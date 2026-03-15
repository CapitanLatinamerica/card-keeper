package com.supersonic.evercard.features.root.data

data class DiscountCard(
    val id: String,
    val name: String,
    val color: Int,       // Цвет фона карты
    val logoRes: Int?,    // Ресурс логотипа (R.drawable...)
    val barcode: String,  // Номер штрих-кода
    val isFavorite: Boolean = false
)
