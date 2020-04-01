package com.education.core_api.extension

import android.content.SharedPreferences

fun SharedPreferences.putString(key: String, value: String): Boolean {
    val editor = this.edit()
    editor.putString(key, value)

    return editor.commit()
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
