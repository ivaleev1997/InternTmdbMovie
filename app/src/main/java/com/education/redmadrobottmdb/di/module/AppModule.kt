package com.education.redmadrobottmdb.di.module

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.education.core_api.PREFS_SHARED_PREFS
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_SHARED_PREFS, MODE_PRIVATE)
    }
}
