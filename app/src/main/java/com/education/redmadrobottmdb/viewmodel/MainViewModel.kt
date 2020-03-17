package com.education.redmadrobottmdb.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.education.core_api.REQUEST_LIFE
import com.education.core_api.REQUEST_TOKEN
import com.education.core_api.SESSION
import java.util.*
import javax.inject.Inject

class MainViewModel
    @Inject constructor(
        private val sharedPrefs: SharedPreferences
    ) : ViewModel() {

    fun isSessionExist(): Boolean =
        isSessionTokenExist() && isRequestTokenExist() && isTokenLifeTimeUp()

    private fun isRequestTokenExist(): Boolean =
         sharedPrefs.getString(REQUEST_TOKEN, "")?.isNotBlank() == true

    private fun isSessionTokenExist(): Boolean =
        sharedPrefs.getString(SESSION, "")?.isNotBlank() == true

    private fun isTokenLifeTimeUp(): Boolean =
        sharedPrefs.getLong(REQUEST_LIFE, 0L) > getCurrentTimeMills()

    private fun getCurrentTimeMills(): Long {
        val calendar = Calendar.getInstance()
        return calendar.time.time
    }
}
