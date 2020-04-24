package com.education.core_api.extension

import android.content.SharedPreferences
import com.education.core_api.BLANK_STR

fun SharedPreferences.putString(key: String, value: String): Boolean {
    val editor = this.edit()
    editor.putString(key, value)

    return editor.commit()
}

fun SharedPreferences.getStringOrBlank(key: String, defValue: String): String {
    return this.getString(key, defValue) ?: BLANK_STR
}

fun SharedPreferences.putLong(key: String, value: Long): Boolean {
    val editor = this.edit()
    editor.putLong(key, value)

    return editor.commit()
}

fun SharedPreferences.clear(): Boolean {
    val editor = this.edit()
    editor.clear()

    return editor.commit()
}
