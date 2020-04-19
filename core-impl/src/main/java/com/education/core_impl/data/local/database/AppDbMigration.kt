package com.education.core_impl.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class AppDbMigration {
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """ALTER TABLE Movie ADD COLUMN isWatched INTEGER DEFAULT 0 NOT NULL"""
                )
            }
        }

        // isWorthWatching заполнятеся из поля isWatched
        val MIGRATION_2_3_BASE = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Создаем новую таблицу
                database.execSQL(
                    """CREATE TABLE IF NOT EXISTS Movie_new (
                                id INTEGER NOT NULL,
                                originalTitle TEXT NOT NULL,
                                posterPath TEXT,
                                overview TEXT NOT NULL,
                                releaseDate TEXT NOT NULL,
                                voteCount INTEGER NOT NULL,
                                voteAverage REAL NOT NULL,
                                title TEXT NOT NULL,
                                popularity REAL NOT NULL,
                                runTime INTEGER NOT NULL,
                                genres TEXT NOT NULL,
                                isWorthWatching INTEGER NOT NULL,
                                PRIMARY KEY(id))"""
                )

                // Переносим данные из старой таблицы
                database.execSQL(
                    """INSERT INTO Movie_new (
                        id,
                        originalTitle,
                        posterPath,
                        overview,
                        releaseDate,
                        voteCount,
                        voteAverage,
                        title,
                        popularity,
                        runTime,
                        genres,
                        isWorthWatching)
                        SELECT
                        id,
                        originalTitle,
                        posterPath,
                        overview,
                        releaseDate,
                        voteCount,
                        voteAverage,
                        title,
                        popularity,
                        runTime,
                        genres,
                        isWatched FROM Movie"""
                )

                // Удаляем старую таблицу
                database.execSQL(
                    """DROP TABLE Movie"""
                )

                // Меняем на требуемое название (movie_new -> movie)
                database.execSQL(
                    """ALTER TABLE Movie_new RENAME TO Movie"""
                )
            }
        }

        // isWorthWatching заполнятеся 1 если voteAverage > 7
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Создаем новую таблицу
                database.execSQL(
                    """CREATE TABLE IF NOT EXISTS Movie_new (
                                id INTEGER NOT NULL,
                                originalTitle TEXT NOT NULL,
                                posterPath TEXT,
                                overview TEXT NOT NULL,
                                releaseDate TEXT NOT NULL,
                                voteCount INTEGER NOT NULL,
                                voteAverage REAL NOT NULL,
                                title TEXT NOT NULL,
                                popularity REAL NOT NULL,
                                runTime INTEGER NOT NULL,
                                genres TEXT NOT NULL,
                                isWorthWatching INTEGER NOT NULL,
                                PRIMARY KEY(id))"""
                )

                // Переносим данные из старой таблицы
                database.execSQL(
                    """INSERT INTO Movie_new (
                        id,
                        originalTitle,
                        posterPath,
                        overview,
                        releaseDate,
                        voteCount,
                        voteAverage,
                        title,
                        popularity,
                        runTime,
                        genres,
                        isWorthWatching)
                        SELECT
                        id,
                        originalTitle,
                        posterPath,
                        overview,
                        releaseDate,
                        voteCount,
                        voteAverage,
                        title,
                        popularity,
                        runTime,
                        genres,
                        case when voteAverage > 7 then 1 else 0 end as isWorthWatching FROM Movie"""
                )

                // Удаляем старую таблицу
                database.execSQL(
                    """DROP TABLE Movie"""
                )

                // Меняем на требуемое название (movie_new -> movie)
                database.execSQL(
                    """ALTER TABLE Movie_new RENAME TO Movie"""
                )
            }
        }
    }
}