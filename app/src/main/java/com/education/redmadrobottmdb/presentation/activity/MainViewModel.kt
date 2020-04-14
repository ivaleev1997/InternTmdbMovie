package com.education.redmadrobottmdb.presentation.activity

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.education.core_api.data.LocalDataSource
import com.education.login.presentation.LoginFragmentDirections
import com.education.pin.presentation.enterpin.EnterPinFragmentDirections
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private var userName: String = ""

    private fun isTokenLifeTimeUp(): Boolean =
        localDataSource.getTokenLifeTime() > getCurrentTimeMills()

    private fun getCurrentTimeMills(): Long {
        val calendar = Calendar.getInstance()
        return calendar.time.time
    }

    fun onLogoutClicked() {
        localDataSource.cleanTokens()
    }

    fun defineScreen(rootNavController: NavController) {
        if (isLoggedIn()) {
            rootNavController.navigate(EnterPinFragmentDirections.toEnterPinFragment(userName))
        } else {
            rootNavController.navigate(LoginFragmentDirections.toLoginFragment())
        }
    }

    private fun isLoggedIn(): Boolean {
        userName = localDataSource.getUserName()

        return userName.isNotEmpty()
    }
}
