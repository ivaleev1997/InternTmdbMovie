package com.education.core_api.mediator

import android.content.SharedPreferences
import android.net.ConnectivityManager

interface AppProvider {

    fun provideShared(): SharedPreferences

    fun provideConnectivityManager(): ConnectivityManager
}