package com.supersonic.evercard.di

import android.content.Context
import android.content.SharedPreferences
import com.supersonic.evercard.features.cards_list.ui.CardListViewModel
import com.supersonic.evercard.features.root.ui.MainFragmentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

// ============================================================
// МОДУЛЬ KOIN (ОПИСАНИЕ ЗАВИСИМОСТЕЙ)
// ============================================================

/**
 * appModule — это объект, описывающий Koin модуль.
 * Здесь мы объявляем все зависимости, которые Koin будет создавать и предоставлять.
 *
 * Функция module { ... } возвращает объект Module, который Koin использует для DI.
 */
val appModule = module {

    // ============================================================
    // ОБЪЯВЛЕНИЕ SHAREDPREFERENCES (SINGLETON)
    // ============================================================

    /**
     * single { ... } означает, что зависимость будет создана один раз и будет
     * существовать как синглтон (один экземпляр на всё приложение).
     *
     * SharedPreferences — идеальный кандидат для синглтона, потому что
     * он уже внутри работает с файлами и кэширует данные.
     *
     * Внутри фигурных скобок мы описываем, КАК создать эту зависимость.
     */
    single<SharedPreferences> {
        provideSharedPreferences(androidContext())
    }

    // Здесь можно добавить другие зависимости:
    // - Репозитории (single { LoyaltyCardRepositoryImpl(get()) })
    // - Базу данных (single { AppDatabase.getInstance(androidContext()) })
    viewModel { MainFragmentViewModel() }
    viewModel { CardListViewModel() }
}

// ============================================================
// ФАБРИЧНАЯ ФУНКЦИЯ ДЛЯ СОЗДАНИЯ SHAREDPREFERENCES
// ============================================================

/**
 * Отдельная функция для создания SharedPreferences.
 *
 * Почему не написать код прямо в модуле?
 * 1. Чистота — модуль не засоряется деталями реализации.
 * 2. Тестируемость — эту функцию можно протестировать отдельно.
 * 3. Переиспользование — если понадобится создать ещё один экземпляр,
 *    можно вызвать функцию снова.
 *
 * @param context контекст приложения (обычно Application Context)
 * @return настроенный экземпляр SharedPreferences
 */
fun provideSharedPreferences(context: Context): SharedPreferences {

    /**
     * getSharedPreferences() — метод Context для получения файла настроек.
     *
     * @param PREFERENCE_NAME — имя файла (константа из AppModule.kt)
     * @param Context.MODE_PRIVATE — режим доступа (только для этого приложения)
     *
     * В современных версиях Android MODE_PRIVATE — это единственный доступный режим.
     * Он означает, что файл может читать только наше приложение.
     */
    return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
}