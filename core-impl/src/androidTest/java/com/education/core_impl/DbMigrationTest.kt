package com.education.core_impl

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.education.core_api.data.local.AppDb
import com.education.core_impl.data.local.database.AppDbImpl
import com.education.core_impl.data.local.database.AppDbMigration.Companion.MIGRATION_1_2
import com.education.core_impl.data.local.database.AppDbMigration.Companion.MIGRATION_2_3
import com.education.core_impl.data.local.database.AppDbMigration.Companion.MIGRATION_2_3_BASE
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
        // Given
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
            close()
        }

        // When and Then
        helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)
    }

    @Test
    @Throws(IOException::class)
    fun migrate2To3() {
        // Given
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
            close()
        }

        // When and Then
        helper.runMigrationsAndValidate(TEST_DB, 3, true, MIGRATION_2_3_BASE)
    }

    @Test
    @Throws(IOException::class)
    fun migrate2To3WithCase() {
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
            close()
        }

        // When and Then
        helper.runMigrationsAndValidate(TEST_DB, 3, true, MIGRATION_2_3)
    }
}
