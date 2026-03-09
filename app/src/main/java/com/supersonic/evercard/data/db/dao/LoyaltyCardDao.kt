package com.supersonic.evercard.data.db.dao

import androidx.room.*
import com.supersonic.evercard.data.db.entity.LoyaltyCardEntity
import kotlinx.coroutines.flow.Flow

/**
 * Dao (Data Access Object) - объект доступа к данным.
 * Это интерфейс, потому что Room сам создаст реализацию.
 *
 * ВСЕ операции с базой данных описываются здесь.
 *
 * @Dao - аннотация для Room, чтобы он понял, что это DAO
 */
@Dao
interface LoyaltyCardDao {

    /**
     * Получить ВСЕ карты, отсортированные по дате создания (новые сверху)
     *
     * Flow - это специальный тип из Kotlin Coroutines.
     * Flow "следит" за данными: если в БД что-то изменится,
     * Flow автоматически пришлёт новый список во все экраны, которые его слушают.
     *
     * @return Flow со списком всех карт
     */
    @Query("SELECT * FROM loyalty_cards ORDER BY createdAt DESC")
    fun getAllCards(): Flow<List<LoyaltyCardEntity>>

    /**
     * Получить только избранные карты (isFavorite = 1)
     * 1 в SQLite означает true, 0 - false
     *
     * @return Flow со списком избранных карт
     */
    @Query("SELECT * FROM loyalty_cards WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteCards(): Flow<List<LoyaltyCardEntity>>

    /**
     * Получить одну конкретную карту по её id
     *
     * suspend - значит, что функцию можно вызывать только из корутины
     * (или из другой suspend-функции). Это защита: операции с БД не должны
     * выполняться в главном потоке (UI).
     *
     * @param cardId идентификатор карты
     * @return карта или null, если не найдена
     */
    @Query("SELECT * FROM loyalty_cards WHERE id = :cardId")
    suspend fun getCardById(cardId: Long): LoyaltyCardEntity?

    /**
     * Вставить новую карту в базу данных
     *
     * @Insert - специальная аннотация Room для вставки
     * @param card объект для вставки
     * @return id новой карты (который сгенерировала БД)
     */
    @Insert
    suspend fun insertCard(card: LoyaltyCardEntity): Long

    /**
     * Обновить существующую карту
     * Room сам найдёт карту по id и обновит все поля
     *
     * @Update - аннотация для обновления
     * @param card карта с новыми даньми (id должен совпадать с существующей)
     */
    @Update
    suspend fun updateCard(card: LoyaltyCardEntity)

    /**
     * Удалить карту из базы
     *
     * @Delete - аннотация для удаления
     * @param card карта для удаления (достаточно чтобы id был правильный)
     */
    @Delete
    suspend fun deleteCard(card: LoyaltyCardEntity)

    /**
     * Поиск карт по названию магазина
     *
     * % || :query || % - это конкатенация (склеивание) строк в SQLite
     * Например, если query = "пят", то получится "%пят%"
     * % означает "любые символы до и после"
     *
     * @param query текст для поиска
     * @return Flow со списком найденных карт
     */
    @Query("SELECT * FROM loyalty_cards WHERE shopName LIKE '%' || :query || '%'")
    fun searchCards(query: String): Flow<List<LoyaltyCardEntity>>
}