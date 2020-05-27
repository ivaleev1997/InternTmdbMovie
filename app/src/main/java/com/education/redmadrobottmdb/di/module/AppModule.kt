package com.education.redmadrobottmdb.di.module

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    companion object {
        const val PREFS_SHARED_PREFS = "tmdb_shared"
    }

    @Provides
    @Singleton
    fun provideSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_SHARED_PREFS, MODE_PRIVATE)
    }
}
