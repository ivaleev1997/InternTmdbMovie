package com.education.core_impl

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.education.core_impl.data.local.database.AppDbImpl
import com.education.core_impl.data.local.database.AppDbMigration.Companion.MIGRATION_1_2
import com.education.core_impl.data.local.database.AppDbMigration.Companion.MIGRATION_2_3
import com.education.core_impl.data.local.database.AppDbMigration.Companion.MIGRATION_2_3_BASE
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class DbMigrationTest {
    private val TEST_DB = "migration-test"

    @Rule
    @JvmField
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDbImpl::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        val expectedRowsCount = 2
        val expectedValueIsWatched = false
        val actualValueFirstRowIsWatched: Boolean
        val actualValueSecondRowIsWatched: Boolean

        var db = helper.createDatabase(TEST_DB, 1).apply {
            execSQL("""
                CREATE TABLE IF NOT EXISTS Movie (
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

        db = helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)

        val cursor = db.query("select * from movie")
        cursor.moveToFirst()
        actualValueFirstRowIsWatched = cursor.getInt(cursor.getColumnIndex("isWatched")) == 1
        cursor.moveToLast()
        actualValueSecondRowIsWatched = cursor.getInt(cursor.getColumnIndex("isWatched")) == 1

        assertEquals(expectedRowsCount, cursor.count)
        assertEquals(expectedValueIsWatched, actualValueFirstRowIsWatched)
        assertEquals(expectedValueIsWatched, actualValueSecondRowIsWatched)

        helper.closeWhenFinished(db)
    }

    @Test
    @Throws(IOException::class)
    fun migrate2To3() {
        val expectedRowsCount = 2
        val expectedValueIsWatched = false
        val actualValueFirstRowIsWatched: Boolean
        val actualValueSecondRowIsWatched: Boolean

        var db = helper.createDatabase(TEST_DB, 2).apply {
            execSQL("""
                CREATE TABLE IF NOT EXISTS Movie (
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

        db = helper.runMigrationsAndValidate(TEST_DB, 3, true, MIGRATION_2_3_BASE)

        val cursor = db.query("select * from movie")
        cursor.moveToFirst()
        actualValueFirstRowIsWatched = cursor.getInt(cursor.getColumnIndex("isWorthWatching")) == 1
        cursor.moveToLast()
        actualValueSecondRowIsWatched = cursor.getInt(cursor.getColumnIndex("isWorthWatching")) == 1

        assertEquals(expectedRowsCount, cursor.count)
        assertEquals(expectedValueIsWatched, actualValueFirstRowIsWatched)
        assertEquals(expectedValueIsWatched, actualValueSecondRowIsWatched)

        helper.closeWhenFinished(db)
    }

    @Test
    @Throws(IOException::class)
    fun migrate2To3WithCase() {
        val expectedValueFirstRow = false
        val expectedValueSecondRow = true

        val actualValueFirstRowIsWatched: Boolean
        val actualValueSecondRowIsWatched: Boolean

        var db = helper.createDatabase(TEST_DB, 2).apply {
            execSQL("""
                CREATE TABLE IF NOT EXISTS Movie (
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

        db = helper.runMigrationsAndValidate(TEST_DB, 3, true, MIGRATION_2_3)
        //val cursorTest = db.query("select id, originalTitle, case when voteAverage > 7 then 1 else 0 end as isWortWatching from movie")
        var cursor = db.query("select * from movie where id == 1903")
        cursor.moveToFirst()
        actualValueFirstRowIsWatched = cursor.getInt(cursor.getColumnIndex("isWorthWatching")) == 1

        cursor = db.query("select * from movie where id == 36557")
        cursor.moveToFirst()
        actualValueSecondRowIsWatched = cursor.getInt(cursor.getColumnIndex("isWorthWatching")) == 1

        assertEquals(expectedValueFirstRow, actualValueFirstRowIsWatched)
        assertEquals(expectedValueSecondRow, actualValueSecondRowIsWatched)

        helper.closeWhenFinished(db)
    }

    private fun insertDataValuesForFirstMigration(db: SupportSQLiteDatabase) {
        db.execSQL("""
                INSERT INTO Movie (
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
                INSERT INTO Movie (
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