package com.supersonic.evercard.domain.model

/**
 * LoyaltyCard - это основная модель данных нашего приложения.
 * Она представляет собой одну карту лояльности (скидочную, накопительную).
 *
 * Эта модель находится в domain слое, потому что она чистая и не знает,
 * как именно мы храним данные (в БД, в файле, в облаке).
 *
 * @param id уникальный идентификатор карты (0 означает, что карта ещё не сохранена)
 * @param shopName название магазина (например, "Пятёрочка", "Лента")
 * @param cardNumber номер карты (может быть как числом, так и штрихкодом)
 * @param barcodeType тип штрихкода (CODE128, QR и т.д.)
 * @param color цвет карточки в интерфейсе (если пользователь выбрал)
 * @param isFavorite избранное или нет
 * @param createdAt когда карта была создана (для сортировки)
 */

data class LoyaltyCard(
    val id: Long = 0,                    // 0 = новая карта, >0 = уже есть в БД
    val shopName: String,                 // Название магазина
    val cardNumber: String,                // Номер карты или код
    val barcodeType: BarcodeType = BarcodeType.CODE128,  // Тип кода
    val color: Int? = null,                // Цвет (null = автоматический)
    val isFavorite: Boolean = false,       // В избранном или нет
    val createdAt: Long = System.currentTimeMillis()  // Время создания
)

/**
 * Типы штрихкодов, которые может генерировать наше приложение.
 *
 * CODE128 - самый популярный тип для скидочных карт
 * QR - квадратный код с точками
 * EAN13 - для товаров (реже, но бывает)
 * UNKNOWN - если не смогли распознать
 */
enum class BarcodeType {
    CODE128,    // Обычный штрихкод
    QR,         // QR-код
    EAN13,      // Европейский товарный номер
    UNKNOWN     // Неизвестный тип
}