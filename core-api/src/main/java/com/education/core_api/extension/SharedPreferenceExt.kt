package com.education.core_api.extension

import android.content.SharedPreferences
import androidx.core.content.edit

fun SharedPreferences.putString(key: String, value: String) {
    this.edit(true) {
        putString(key, value)
    }
}

fun SharedPreferences.putLong(key: String, value: Long) {
    this.edit(true) {
        putLong(key, value)
    }
}

fun SharedPreferences.clear() {
    this.edit(true) {
        this.clear()
    }
}
