package com.supersonic.evercard.data.db.mapper

import com.supersonic.evercard.data.db.entity.LoyaltyCardEntity
import com.supersonic.evercard.domain.model.BarcodeType
import com.supersonic.evercard.domain.model.LoyaltyCard

/**
 * CardMapper - это объект (object, а не class), потому что ему не нужно
 * хранить состояние. Это просто набор функций-переводчиков.
 *
 * object в Kotlin = синглтон (один экземпляр на всё приложение)
 */

object CardMapper {

    /**
     * Преобразует Entity (из БД) в Domain модель (для UI)
     *
     * @param entity модель из Room (то, что достали из БД)
     * @return готовая модель для использования в приложении
     */
    fun mapEntityToDomain(entity: LoyaltyCardEntity): LoyaltyCard {
        return LoyaltyCard(
            id = entity.id,
            shopName = entity.shopName,
            cardNumber = entity.cardNumber,
            // Здесь нужно преобразовать String обратно в BarcodeType
            barcodeType = mapStringToBarcodeType(entity.barcodeType),
            color = entity.color,
            isFavorite = entity.isFavorite,
            createdAt = entity.createdAt
        )
    }

    /**
     * Преобразует Domain модель (из UI) в Entity (для сохранения в БД)
     *
     * @param domain модель из приложения (например, то, что создал пользователь)
     * @return модель для Room
     */
    fun mapDomainToEntity(domain: LoyaltyCard): LoyaltyCardEntity {
        return LoyaltyCardEntity(
            id = domain.id,
            shopName = domain.shopName,
            cardNumber = domain.cardNumber,
            // Здесь enum преобразуем в строку (CODE128.name = "CODE128")
            barcodeType = domain.barcodeType.name,
            color = domain.color,
            isFavorite = domain.isFavorite,
            createdAt = domain.createdAt
        )
    }

    /**
     * Вспомогательная функция для преобразования строки в enum BarcodeType
     *
     * @param type строка из БД (например, "CODE128")
     * @return соответствующий enum или UNKNOWN, если строка не подходит
     */
    private fun mapStringToBarcodeType(type: String): BarcodeType {
        return try {
            // Пытаемся преобразовать строку в enum
            // Например, "CODE128" -> BarcodeType.CODE128
            BarcodeType.valueOf(type)
        } catch (e: IllegalArgumentException) {
            // Если строка не подходит (например, "SOMETHING_ELSE"),
            // возвращаем UNKNOWN, чтобы приложение не упало
            BarcodeType.UNKNOWN
        }
    }
}