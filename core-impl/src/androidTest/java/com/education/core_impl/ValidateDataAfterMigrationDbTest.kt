package com.education.core_impl

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.education.core_api.data.local.AppDb
import com.education.core_impl.data.local.database.AppDbImpl
import com.education.core_impl.data.local.database.AppDbMigration
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ValidateDataAfterMigrationDbTest {

    private val TEST_DB = "migration-test"

    @Rule
    @JvmField
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDbImpl::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun validateDataAfter1to2MigrationTest() {
        // Given
        val expectedRowsCount = 2
        val expectedValueIsWatched = false
        val actualValueFirstRowIsWatched: Boolean
        val actualValueSecondRowIsWatched: Boolean

        helper.createDatabase(TEST_DB, 1).apply {
            execSQL("""
                CREATE TABLE IF NOT EXISTS ${AppDb.APP_DB_TABLE_MOVIE} (
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
                PRIMARY KEY(id))
            """)
            insertDataValuesForFirstMigration(this)
            close()
        }

        // When
        val db = helper.runMigrationsAndValidate(TEST_DB, 2, true, AppDbMigration.MIGRATION_1_2)
        val cursor = db.query("select * from ${AppDb.APP_DB_TABLE_MOVIE}")
        cursor.moveToFirst()
        actualValueFirstRowIsWatched = cursor.getInt(cursor.getColumnIndex("isWatched")) == 1
        cursor.moveToLast()
        actualValueSecondRowIsWatched = cursor.getInt(cursor.getColumnIndex("isWatched")) == 1

        // Then
        assertEquals(expectedRowsCount, cursor.count)
        assertEquals(expectedValueIsWatched, actualValueFirstRowIsWatched)
        assertEquals(expectedValueIsWatched, actualValueSecondRowIsWatched)

        helper.closeWhenFinished(db)
    }

    @Test
    fun validateDataAfter2to3MigrationTest() {
        // Given
        val expectedRowsCount = 2
        val expectedValueIsWatched = false
        val actualValueFirstRowIsWatched: Boolean
        val actualValueSecondRowIsWatched: Boolean

        helper.createDatabase(TEST_DB, 2).apply {
            execSQL("""
                CREATE TABLE IF NOT EXISTS ${AppDb.APP_DB_TABLE_MOVIE} (
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
                isWatched INTEGER DEFAULT 0 NOT NULL,
                PRIMARY KEY(id))
            """)
            insertDataValuesForFirstMigration(this)
            close()
        }

        // When
        val db = helper.runMigrationsAndValidate(TEST_DB, 3, true, AppDbMigration.MIGRATION_2_3_BASE)
        val cursor = db.query("select * from ${AppDb.APP_DB_TABLE_MOVIE}")
        cursor.moveToFirst()
        actualValueFirstRowIsWatched = cursor.getInt(cursor.getColumnIndex("isWorthWatching")) == 1
        cursor.moveToLast()
        actualValueSecondRowIsWatched = cursor.getInt(cursor.getColumnIndex("isWorthWatching")) == 1

        // Then
        assertEquals(expectedRowsCount, cursor.count)
        assertEquals(expectedValueIsWatched, actualValueFirstRowIsWatched)
        assertEquals(expectedValueIsWatched, actualValueSecondRowIsWatched)

        helper.closeWhenFinished(db)
    }

    @Test
    fun validateDataAfter2to3WithCaseMigrationTest() {
        // Given
        val expectedValueFirstRow = false
        val expectedValueSecondRow = true

        val actualValueFirstRowIsWatched: Boolean
        val actualValueSecondRowIsWatched: Boolean

        helper.createDatabase(TEST_DB, 2).apply {
            execSQL("""
                CREATE TABLE IF NOT EXISTS ${AppDb.APP_DB_TABLE_MOVIE} (
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
                isWatched INTEGER DEFAULT 0 NOT NULL,
                PRIMARY KEY(id))
            """)

            insertDataValuesForFirstMigration(this)

            close()
        }

        // When
        val db = helper.runMigrationsAndValidate(TEST_DB, 3, true, AppDbMigration.MIGRATION_2_3)
        var cursor = db.query("select * from ${AppDb.APP_DB_TABLE_MOVIE} where id == 1903")
        cursor.moveToFirst()
        actualValueFirstRowIsWatched = cursor.getInt(cursor.getColumnIndex("isWorthWatching")) == 1

        cursor = db.query("select * from ${AppDb.APP_DB_TABLE_MOVIE} where id == 36557")
        cursor.moveToFirst()
        actualValueSecondRowIsWatched = cursor.getInt(cursor.getColumnIndex("isWorthWatching")) == 1

        // Then
        assertEquals(expectedValueFirstRow, actualValueFirstRowIsWatched)
        assertEquals(expectedValueSecondRow, actualValueSecondRowIsWatched)

        helper.closeWhenFinished(db)
    }

    private fun insertDataValuesForFirstMigration(db: SupportSQLiteDatabase) {
        db.execSQL("""
                INSERT INTO ${AppDb.APP_DB_TABLE_MOVIE} (
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
                        genres)
                        VALUES (
                        1903,
                        "Vanilla Sky",
                        "/v9uych9hmfnc4gTBWeQu0k47RIp.jpg",
                        "У богатого плэйбоя Дэвида было все:",
                        "2001-01-22",
                        2357,
                        6.8,
                        "Ванильное небо",
                        12.992,
                        136,
                        "мелодрама, фантастика, фэнтези, драма, триллер"
                        )
            """)

        db.execSQL("""
                INSERT INTO ${AppDb.APP_DB_TABLE_MOVIE} (
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
                        genres)
                        VALUES (
                        36557,
                        "Casino Royale",
                        "/6qfAK81FC6FlbvF0cZuRCZtnaIk.jpg",
                        "Самый известный шпион",
                        "2006-11-14",
                        6933,
                        7.5,
                        "Казино Рояль",
                        37.078,
                        139,
                        "приключения, боевик, триллер"
                        )
            """)
    }
}
