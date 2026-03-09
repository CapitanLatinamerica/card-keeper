package com.supersonic.evercard.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.supersonic.evercard.data.db.dao.LoyaltyCardDao
import com.supersonic.evercard.data.db.entity.LoyaltyCardEntity

/**
 * AppDatabase - это точка входа в базу данных Room.
 *
 * @Database - анноция, которая говорит Room:
 * - entities = [LoyaltyCardEntity::class] - список таблиц (классов Entity)
 * - version = 1 - версия базы (нужно увеличивать при изменении структуры)
 * - exportSchema = false - не экспортировать схему (для простоты)
 */
@Database(
    entities = [LoyaltyCardEntity::class],  // Таблицы в базе
    version = 1,                            // Текущая версия
    exportSchema = false                     // Не сохранять схему
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Абстрактный метод, который Room реализует автоматически.
     * Через него мы будем получать наш Dao.
     */
    abstract fun loyaltyCardDao(): LoyaltyCardDao

    companion object {
        /**
         * @Volatile - гарантирует, что изменения INSTANCE сразу видны всем потокам
         * Это важно для многопоточности.
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Получить экземпляр базы данных (синглтон).
         *
         * Паттерн "Double-checked locking" - гарантирует,
         * что база создастся только один раз, даже если вызвать
         * этот метод из разных потоков одновременно.
         *
         * @param context нужен для доступа к файловой системе
         * @return экземпляр AppDatabase
         */
        fun getInstance(context: Context): AppDatabase {
            // Первая проверка: если уже есть, просто возвращаем
            return INSTANCE ?: synchronized(this) {
                // Вторая проверка: могло создаться в другом потоке
                val instance = Room.databaseBuilder(
                    context.applicationContext,  // Контекст приложения (не Activity!)
                    AppDatabase::class.java,     // Класс нашей БД
                    "evercard_database"           // Имя файла базы данных
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}