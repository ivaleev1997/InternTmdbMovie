package com.education.redmadrobottmdb.presentation.activity

import androidx.lifecycle.ViewModel
import com.education.core_api.data.LocalDataSource
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val localDataSource: LocalDataSource
) : ViewModel() {

    fun isSessionExist(): Boolean =
        isSessionTokenExist() && isRequestTokenExist() && isTokenLifeTimeUp()

    private fun isRequestTokenExist(): Boolean =
        localDataSource.getRequestToken().isNotBlank()

    private fun isSessionTokenExist(): Boolean =
        localDataSource.getSessionId().isNotBlank()

    private fun isTokenLifeTimeUp(): Boolean =
        localDataSource.getTokenLifeTime() > getCurrentTimeMills()

    private fun getCurrentTimeMills(): Long {
        val calendar = Calendar.getInstance()
        return calendar.time.time
    }

    fun onLogoutClicked() {
        localDataSource.cleanTokens()
    }

}
