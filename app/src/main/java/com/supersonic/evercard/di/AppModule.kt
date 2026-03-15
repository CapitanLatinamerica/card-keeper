package com.supersonic.evercard.di

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import androidx.core.content.edit

// ============================================================
// КОНСТАНТЫ ДЛЯ РАБОТЫ С НАСТРОЙКАМИ
// ============================================================

/**
 * Имя файла настроек (SharedPreferences), где будут храниться пользовательские настройки.
 * В Android это создаст XML-файл с указанным именем.
 */
const val PREFERENCE_NAME = "user_preferences"

/**
 * Ключ, по которому в SharedPreferences будет храниться информация о выбранной теме.
 * Значение будет булевым: true = темная тема, false = светлая.
 */
const val PREFERENCE_THEME_KEY = "is_dark_theme_enabled"

// ============================================================
// ЗАКРЫТЫЙ КЛАСС ДЛЯ ТИПИЗАЦИИ ТЕМ (Type-Safe подход)
// ============================================================

/**
 * sealed class (закрытый класс) — это способ ограничить иерархию классов.
 * Здесь мы используем его, чтобы определить все возможные темы приложения.
 * Это безопаснее, чем использовать просто Int или Boolean, потому что
 * компилятор будет проверять, что мы обработали все варианты.
 *
 * @param mode числовое значение, которое понимает AppCompatDelegate (MODE_NIGHT_NO, MODE_NIGHT_YES и т.д.)
 */
sealed class ThemeMode(val mode: Int) {

    /**
     * Светлая тема. Использует константу MODE_NIGHT_NO из AppCompatDelegate.
     */
    data object Light : ThemeMode(AppCompatDelegate.MODE_NIGHT_NO)

    /**
     * Тёмная тема. Использует константу MODE_NIGHT_YES из AppCompatDelegate.
     */
    data object Dark : ThemeMode(AppCompatDelegate.MODE_NIGHT_YES)

    /**
     * Companion object — это аналог "статического блока" в Java.
     * Здесь хранятся вспомогательные функции для работы с темами.
     */
    companion object {

        /**
         * Преобразует булево значение (true/false) в объект ThemeMode.
         * Это удобно, когда мы читаем настройку из SharedPreferences.
         *
         * @param isDark true = тёмная тема, false = светлая
         * @return соответствующий объект (Light или Dark)
         */
        fun fromBoolean(isDark: Boolean): ThemeMode {
            return if (isDark) Dark else Light
        }
    }
}

// ============================================================
// ОСНОВНОЙ КЛАСС ПРИЛОЖЕНИЯ (Application)
// ============================================================

/**
 * Класс Application — это глобальный контекст всего приложения.
 * Он создаётся самым первым при запуске и живёт, пока приложение не будет убито.
 *
 * Здесь мы:
 * 1. Инициализируем Koin (внедрение зависимостей)
 * 2. Устанавливаем тему приложения из сохранённых настроек
 * 3. Храним метод для переключения темы
 *
 * Наследуем KoinComponent, чтобы иметь доступ к get() для получения зависимостей.
 */
class AppModule : Application(), KoinComponent {

    /**
     * Текущая тема приложения. Хранится в памяти для быстрого доступа.
     * По умолчанию ставим светлую тему (Light).
     */
    private var currentTheme: ThemeMode = ThemeMode.Light

    /**
     * onCreate() вызывается Android-системой при создании процесса приложения.
     * Это первое место, где выполняется наш код.
     */
    override fun onCreate() {
        super.onCreate() // Всегда вызываем родительский метод!

        // ============================================================
        // НАСТРОЙКА KOIN (ВНЕДРЕНИЕ ЗАВИСИМОСТЕЙ)
        // ============================================================

        /**
         * startKoin — это точка входа для Koin.
         * Здесь мы настраиваем DI-контейнер.
         */
        startKoin {
            // androidContext() передаёт Koin'у контекст приложения,
            // чтобы он мог создавать Android-специфичные зависимости
            androidContext(this@AppModule)

            /**
             * modules() — список модулей с зависимостями.
             * appModule — это наш модуль, определённый в файле di/AppModule.kt
             * (или в этом же файле, если он рядом)
             */
            modules(appModule)

            /**
             * logger() — подключаем логирование Koin.
             * Уровень DEBUG будет показывать много информации о создании зависимостей.
             * Это очень полезно при отладке, но можно убрать в релизе.
             */
            logger(AndroidLogger(Level.DEBUG))
        }

        // ============================================================
        // ЧТЕНИЕ ТЕМЫ ИЗ НАСТРОЕК И ПРИМЕНЕНИЕ
        // ============================================================

        /**
         * get() — это функция Koin, которая достаёт зависимость из DI-контейнера.
         * Здесь мы запрашиваем SharedPreferences (они объявлены в appModule).
         *
         * Важно: это работает, потому что мы уже вызвали startKoin выше.
         */
        val preferences: SharedPreferences = get()

        /**
         * Читаем булево значение из настроек. Если ключ ещё не существует,
         * используем false (светлая тема) как значение по умолчанию.
         */
        val isDarkTheme = preferences.getBoolean(PREFERENCE_THEME_KEY, false)

        /**
         * Преобразуем булево значение в объект ThemeMode (Light или Dark).
         */
        currentTheme = ThemeMode.fromBoolean(isDarkTheme)

        /**
         * Устанавливаем тему для всего приложения.
         * AppCompatDelegate.setDefaultNightMode() применяет тему глобально.
         * Это повлияет на все Activity, которые будут созданы после этого момента.
         */
        AppCompatDelegate.setDefaultNightMode(currentTheme.mode)
    }

    // ============================================================
    // МЕТОД ДЛЯ ПЕРЕКЛЮЧЕНИЯ ТЕМЫ (ВЫЗЫВАЕТСЯ ИЗ НАСТРОЕК)
    // ============================================================

    /**
     * Публичный метод для переключения темы во время работы приложения.
     * Обычно вызывается из SettingsFragment или SettingsViewModel.
     *
     * @param isDarkTheme true — включить тёмную тему, false — светлую
     */
    fun switchTheme(isDarkTheme: Boolean) {
        // Получаем SharedPreferences из DI (каждый раз, чтобы быть уверенными в актуальности)
        val preferences: SharedPreferences = get()

        /**
         * Сохраняем новое значение темы в SharedPreferences.
         * edit() открывает редактор, putBoolean() кладёт значение, apply() сохраняет асинхронно.
         */
        preferences.edit { putBoolean(PREFERENCE_THEME_KEY, isDarkTheme) }

        // Обновляем текущую тему в памяти
        currentTheme = ThemeMode.fromBoolean(isDarkTheme)

        /**
         * Применяем новую тему глобально.
         * Важно: это изменит тему только для новых Activity.
         * Текущая Activity может не обновиться автоматически — нужно будет пересоздать её
         * или обработать изменение внутри неё.
         */
        AppCompatDelegate.setDefaultNightMode(currentTheme.mode)
    }
}