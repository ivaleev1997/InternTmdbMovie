package com.education.redmadrobottmdb.presentation

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.education.core_api.PREFS_REQUEST_LIFE
import com.education.core_api.PREFS_REQUEST_TOKEN
import com.education.core_api.PREFS_SESSION
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val sharedPrefs: SharedPreferences
) : ViewModel() {

    fun isSessionExist(): Boolean =
        isSessionTokenExist() && isRequestTokenExist() && isTokenLifeTimeUp()

    private fun isRequestTokenExist(): Boolean =
         sharedPrefs.getString(PREFS_REQUEST_TOKEN, "")?.isNotBlank() == true

    private fun isSessionTokenExist(): Boolean =
        sharedPrefs.getString(PREFS_SESSION, "")?.isNotBlank() == true

    private fun isTokenLifeTimeUp(): Boolean =
        sharedPrefs.getLong(PREFS_REQUEST_LIFE, 0L) > getCurrentTimeMills()

    private fun getCurrentTimeMills(): Long {
        val calendar = Calendar.getInstance()
        return calendar.time.time
    }
}
