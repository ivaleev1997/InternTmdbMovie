package com.education.core_api.di

import android.content.SharedPreferences

interface AppProvider {

    fun provideShared(): SharedPreferences
}
