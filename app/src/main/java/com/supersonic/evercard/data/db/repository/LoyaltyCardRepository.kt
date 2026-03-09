package com.supersonic.evercard.data.db.repository

import com.supersonic.evercard.domain.model.LoyaltyCard
import kotlinx.coroutines.flow.Flow

/**
 * LoyaltyCardRepository - интерфейс репозитория.
 *
 * Зачем нужен интерфейс?
 * 1. Мы можем заменить реализацию (например, для тестов)
 * 2. Мы чётко определяем, что умеет репозиторий
 * 3. Фичи (экраны) зависят от интерфейса, а не от конкретной реализации
 *
 * Все методы повторяют методы Dao, но работают с доменной моделью (LoyaltyCard),
 * а не с Entity. Маппинг будет внутри реализации.
 */
interface LoyaltyCardRepository {

    /**
     * Получить все карты
     * @return Flow со списком карт (уже преобразованных в доменные модели)
     */
    fun getAllCards(): Flow<List<LoyaltyCard>>

    /**
     * Получить избранные карты
     * @return Flow со списком избранных карт
     */
    fun getFavoriteCards(): Flow<List<LoyaltyCard>>

    /**
     * Получить карту по id
     * @param cardId идентификатор
     * @return карта или null
     */
    suspend fun getCardById(cardId: Long): LoyaltyCard?

    /**
     * Добавить новую карту
     * @param card данные карты
     * @return id созданной карты
     */
    suspend fun insertCard(card: LoyaltyCard): Long

    /**
     * Обновить карту
     * @param card карта с новыми данными
     */
    suspend fun updateCard(card: LoyaltyCard)

    /**
     * Удалить карту
     * @param card карта для удаления
     */
    suspend fun deleteCard(card: LoyaltyCard)

    /**
     * Поиск карт
     * @param query строка поиска
     * @return Flow с результатами поиска
     */
    fun searchCards(query: String): Flow<List<LoyaltyCard>>
}