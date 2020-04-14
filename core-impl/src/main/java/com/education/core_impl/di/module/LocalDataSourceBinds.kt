package com.education.core_impl.di.module

import com.education.core_api.data.LocalDataSource
import com.education.core_impl.data.local.LocalDataSourceImpl
import com.education.core_impl.data.local.Security
import com.education.core_impl.data.local.encryption.Encryption
import com.education.core_impl.data.local.encryption.EncryptionImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class LocalDataSourceBinds {

    @Binds
    abstract fun bindsEncryption(encryptionImpl: EncryptionImpl): Encryption

    @Binds
    abstract fun bindsLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    @Module
    companion object {
        @Provides
        @JvmStatic
        @Singleton
        fun provideSecurity(encryption: Encryption) = Security(encryption)
    }
}
