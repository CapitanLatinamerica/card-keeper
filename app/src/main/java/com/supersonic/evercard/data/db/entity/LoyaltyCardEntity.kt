package com.supersonic.evercard.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * LoyaltyCardEntity - это модель для базы данных (Room).
 *
 * Зачем нужна отдельная модель, если у нас уже есть LoyaltyCard?
 * Потому что:
 * 1. Room требует специфических аннотаций (@Entity, @PrimaryKey)
 * 2. В БД мы можем хранить данные немного иначе (например, enum как String)
 * 3. Это разделение защищает domain слой от изменений в БД
 *
 * @Entity(tableName = "loyalty_cards") - говорит Room, что это таблица
 * tableName - как будет называться таблица в БД
 */

@Entity(tableName = "loyalty_cards")
data class LoyaltyCardEntity(

    /**
     * @PrimaryKey(autoGenerate = true) - это уникальный идентификатор
     * autoGenerate = true значит, что Room сам будет назначать id
     * для новых карт (1, 2, 3, ...)
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /**
     * Название магазина - просто строка
     */
    val shopName: String,

    /**
     * Номер карты - тоже строка (даже если это просто цифры)
     */
    val cardNumber: String,

    /**
     * Тип штрихкода - храним как String, потому что Room не умеет
     * напрямую хранить enum'ы (без конвертера)
     */
    val barcodeType: String,  // Будем хранить "CODE128", "QR" и т.д.

    /**
     * Цвет - может быть null, поэтому Int? (знак вопроса)
     * В БД это будет колонка, которая может быть пустой
     */
    val color: Int? = null,

    /**
     * Избранное - 0 или 1 в БД (false/true)
     */
    val isFavorite: Boolean = false,

    /**
     * Время создания в миллисекундах.
     * Используем Long, потому что System.currentTimeMillis() возвращает Long
     */
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Важное замечание:
 * Мы не пишем здесь логику! Только данные.
 * Все преобразования (Entity -> Domain) будут в отдельном файле (Mapper).
 */