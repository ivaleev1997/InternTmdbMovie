package com.education.core_impl.di.module

import android.content.Context
import androidx.room.Room
import com.education.core_api.data.LocalDataSource
import com.education.core_api.data.local.AppDb
import com.education.core_api.data.local.dao.MovieDao
import com.education.core_impl.data.local.LocalDataSourceImpl
import com.education.core_impl.data.local.Security
import com.education.core_impl.data.local.database.AppDbImpl
import com.education.core_impl.data.local.database.AppDbMigration
import com.education.core_impl.data.local.encryption.Encryption
import com.education.core_impl.data.local.encryption.EncryptionImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton

@Module
abstract class LocalDataSourceModule {

    @Binds
    abstract fun bindsEncryption(encryptionImpl: EncryptionImpl): Encryption

    @Binds
    abstract fun bindsLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    @Module
    companion object {
        private const val APPLICATION_DB = "app_db"

        @Provides
        @JvmStatic
        @Singleton
        fun provideSecurity(encryption: Encryption) = Security(encryption)

        @Provides
        @JvmStatic
        @Singleton
        fun provideAppDb(appContext: Context): AppDb {
            return Room.databaseBuilder(appContext, AppDbImpl::class.java, APPLICATION_DB)
                .addMigrations(AppDbMigration.MIGRATION_1_2, AppDbMigration.MIGRATION_2_3)
                .build()
        }

        @Provides
        @JvmStatic
        @Reusable
        fun provideMovieDao(appDb: AppDb): MovieDao {
            return appDb.getMovieDao()
        }
    }
}
