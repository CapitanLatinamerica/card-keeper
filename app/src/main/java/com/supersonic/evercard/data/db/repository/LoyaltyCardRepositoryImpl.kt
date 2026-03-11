package com.supersonic.evercard.data.db.repository

import com.supersonic.evercard.data.db.dao.LoyaltyCardDao
import com.supersonic.evercard.data.db.mapper.CardMapper
import com.supersonic.evercard.domain.model.LoyaltyCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * LoyaltyCardRepositoryImpl - реализация репозитория.
 *
 * @param dao - наш Dao для работы с БД (Room сам даст реализацию)
 *
 * В конструкторе мы получаем зависимости (Dao).
 * Это называется "внедрение зависимостей" (DI).
 */
class LoyaltyCardRepositoryImpl(
    private val dao: LoyaltyCardDao
) : LoyaltyCardRepository {

    /**
     * Получить все карты
     *
     * .map { ... } - это функция Flow, которая преобразует каждый элемент потока
     * Здесь: List<LoyaltyCardEntity> -> List<LoyaltyCard>
     */
    override fun getAllCards(): Flow<List<LoyaltyCard>> {
        return dao.getAllCards().map { entities ->
            // entities - это List<LoyaltyCardEntity> из БД.
            // Преобразуем каждый элемент с помощью маппера
            entities.map { entity ->
                CardMapper.mapEntityToDomain(entity)
            }
        }
    }

    /**
     * Получить избранные карты
     * Всё то же самое, только вызываем другой метод Dao
     */
    override fun getFavoriteCards(): Flow<List<LoyaltyCard>> {
        return dao.getFavoriteCards().map { entities ->
            entities.map { CardMapper.mapEntityToDomain(it) }
        }
    }

    /**
     * Получить карту по id
     *
     * Здесь не Flow, а suspend, потому что это одноразовый запрос
     * (не нужно следить за изменениями)
     */
    override suspend fun getCardById(cardId: Long): LoyaltyCard? {
        // Получаем Entity из БД
        val entity = dao.getCardById(cardId)
        // Если не null, преобразуем, иначе возвращаем null
        return entity?.let { CardMapper.mapEntityToDomain(it) }
    }

    /**
     * Добавить новую карту
     *
     * Сначала преобразуем доменную модель в Entity,
     * потом сохраняем в БД
     */
    override suspend fun insertCard(card: LoyaltyCard): Long {
        val entity = CardMapper.mapDomainToEntity(card)
        return dao.insertCard(entity)
    }

    /**
     * Обновить карту
     *
     * Аналогично insert, только вызываем update
     */
    override suspend fun updateCard(card: LoyaltyCard) {
        val entity = CardMapper.mapDomainToEntity(card)
        dao.updateCard(entity)
    }

    /**
     * Удалить карту
     */
    override suspend fun deleteCard(card: LoyaltyCard) {
        val entity = CardMapper.mapDomainToEntity(card)
        dao.deleteCard(entity)
    }

    /**
     * Поиск карт
     *
     * Здесь нужно быть внимательным: query может быть пустым
     */
    override fun searchCards(query: String): Flow<List<LoyaltyCard>> {
        // Если поисковый запрос пустой, возвращаем все карты
        return if (query.isBlank()) {
            getAllCards()
        } else {
            dao.searchCards(query).map { entities ->
                entities.map { CardMapper.mapEntityToDomain(it) }
            }
        }
    }
}