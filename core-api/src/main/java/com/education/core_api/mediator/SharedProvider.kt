package com.education.core_api.mediator

import android.content.SharedPreferences

interface SharedProvider {

    fun provideShared(): SharedPreferences
}