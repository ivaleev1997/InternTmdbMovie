package com.education.core_api.di

import android.content.SharedPreferences
import android.net.ConnectivityManager

interface AppProvider {

    fun provideShared(): SharedPreferences

    fun provideConnectivityManager(): ConnectivityManager
}
