package com.education.redmadrobottmdb.presentation.activity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.education.core_api.data.LocalDataSource
import com.education.core_api.extension.delegate
import com.education.core_api.presentation.uievent.NavigateToEvent
import com.education.core_api.presentation.viewmodel.BaseViewModel
import com.education.login.presentation.LoginFragmentDirections
import com.education.pin.presentation.enterpin.EnterPinFragmentDirections
import com.education.redmadrobottmdb.domain.MainActivityViewState
import com.education.redmadrobottmdb.domain.RootStatus
import com.scottyab.rootbeer.RootBeer
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val localDataSource: LocalDataSource
) : BaseViewModel() {

    companion object {
        private const val MAX_TIME_ON_BACKGROUND_2_MIN_IN_MILLS = 2 * 60 * 1000
        private const val FIRST_ON_STOP_TIME = 0L
    }

    val liveState = MutableLiveData(MainActivityViewState())
    private var state:MainActivityViewState by liveState.delegate()

    private var userName: String = ""
    private var isRootedDevice = false

    private fun isTokenLifeTimeUp(): Boolean =
        localDataSource.getTokenLifeTime() > getCurrentTimeMills()

    private fun getCurrentTimeMills(): Long {
        val calendar = Calendar.getInstance()
        return calendar.time.time
    }

    fun onLogoutClicked() {
        localDataSource.cleanTokens()
    }

    fun defineScreen() {
        if (!isRootedDevice)
            if (isLoggedIn()) {
                sendEvent(NavigateToEvent(EnterPinFragmentDirections.toEnterPinFragment(userName)))
            } else {
                sendEvent(NavigateToEvent(LoginFragmentDirections.toLoginFragment()))
            }
    }

    private fun checkRoot(context: Context) {
        val rootBeer = RootBeer(context)
        // Добавил знак "!" так как тестирую на эмуляторе!
        if (!rootBeer.isRooted) {
            state = state.copy(rootStatus = RootStatus.ROOTED)
            isRootedDevice = true
        } else {
            state = state.copy(rootStatus = RootStatus.NOT_ROOTED)
        }
    }

    private fun isLoggedIn(): Boolean {
        userName = localDataSource.getUserName()

        return userName.isNotEmpty()
    }

    fun onCreate(context: Context) {
        localDataSource.clearLastOnStopTime()
        checkRoot(context)
    }

    fun onStop() {
        saveLastOnStopTime()
    }

    fun onStart() {
        checkBackgroundTime()
    }

    private fun saveLastOnStopTime() {
        localDataSource.saveOnStopTime(getCurrentTimeMills())
    }

    private fun checkBackgroundTime() {
        val currentTime = getCurrentTimeMills()
        val lastOnStopTime = localDataSource.getOnStopTime()
        if (isLoggedIn() && lastOnStopTime != FIRST_ON_STOP_TIME && (currentTime - lastOnStopTime) > MAX_TIME_ON_BACKGROUND_2_MIN_IN_MILLS)
            sendEvent(NavigateToEvent(EnterPinFragmentDirections.toEnterPinFragment(userName)))
    }
}
