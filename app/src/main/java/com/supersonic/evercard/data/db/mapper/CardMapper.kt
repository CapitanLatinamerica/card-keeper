package com.supersonic.evercard.data.db.mapper

import com.supersonic.evercard.data.db.entity.LoyaltyCardEntity
import com.supersonic.evercard.domain.model.BarcodeType
import com.supersonic.evercard.domain.model.LoyaltyCard

object CardMapper {
    fun mapEntityToDomain(entity: LoyaltyCardEntity): LoyaltyCard {
        return LoyaltyCard(
            id = entity.id,
            shopName = entity.shopName,
            cardNumber = entity.cardNumber,
            barcodeType = mapStringToBarcodeType(entity.barcodeType),
            color = entity.color,
            isFavorite = entity.isFavorite,
            createdAt = entity.createdAt
        )
    }

    fun mapDomainToEntity(domain: LoyaltyCard): LoyaltyCardEntity {
        return LoyaltyCardEntity(
            id = domain.id,
            shopName = domain.shopName,
            cardNumber = domain.cardNumber,
            barcodeType = domain.barcodeType.name,
            color = domain.color,
            isFavorite = domain.isFavorite,
            createdAt = domain.createdAt
        )
    }

    private fun mapStringToBarcodeType(type: String): BarcodeType {
        return try {
            BarcodeType.valueOf(type)
        } catch (e: IllegalArgumentException) {
            BarcodeType.UNKNOWN
        }
    }
}