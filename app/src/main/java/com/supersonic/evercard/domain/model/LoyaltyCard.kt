package com.supersonic.evercard.domain.model

data class LoyaltyCard (
    val id: Long = 0,
    val shopName: String,
    val cardNumber: String,
    val barcodeType: BarcodeType = BarcodeType.CODE128,
    val color: Int? = null,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class BarcodeType {
    CODE128, QR, EAN13, UNKNOWN
}