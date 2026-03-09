package com.supersonic.evercard.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loyalty_cards")
data class LoyaltyCardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val shopName: String,
    val cardNumber: String,
    val barcodeType: String,  // В БД храним как String
    val color: Int? = null,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)