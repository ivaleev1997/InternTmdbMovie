package com.education.pin.presentation

import android.content.Context
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.lifecycle.MutableLiveData
import com.education.core_api.extension.delegate
import com.education.core_api.presentation.uievent.LogoutEvent
import com.education.core_api.presentation.viewmodel.BaseViewModel
import com.education.pin.biometric.BiometricSecurity
import com.education.pin.domain.entity.EnterKeyStatus
import com.education.pin.domain.entity.PinViewState
import timber.log.Timber
import java.util.*

abstract class PinViewModel : BaseViewModel() {
    companion object {
        const val PIN_NUMBERS_COUNT_4 = 4
    }

    lateinit var biometricSecurity: BiometricSecurity
    val liveState = MutableLiveData(PinViewState())
    protected var state: PinViewState by liveState.delegate()

    var isSecondDeque = false
        protected set

    var wasError = false
        protected set

    protected var isBiometricEnable = false

    protected val firstDeque: Deque<Int> = LinkedList<Int>()
    protected val secondDeque: Deque<Int> = LinkedList<Int>()

    protected fun withSecondDeque(number: Int) {
        if (secondDeque.size < PIN_NUMBERS_COUNT_4) {
            secondDeque.push(number)
            state = state.copy(
                enterKeyStatus = EnterKeyStatus.ENTER
            )
            if (secondDeque.size == PIN_NUMBERS_COUNT_4) {
                checkPins()
            }
        }
    }

    protected fun checkBiometric(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val biometricManager = BiometricManager.from(context)
            when (biometricManager.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    Timber.d( "App can authenticate using biometrics.")
                    isBiometricEnable = true
                }
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                    Timber.d("No biometric features available on this device.")
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                    Timber.d("Biometric features are currently unavailable.")
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                    Timber.d("The user hasn't associated any biometric credentials with their account.")
            }
        }
    }

    fun clearDeques(vararg deques: Deque<Int> ) {
        wasError = false
        state = state.copy(enterKeyStatus = EnterKeyStatus.CLEAN)
        deques.forEach { deque ->
            deque.clear()
        }
    }

    protected fun backSpaceOnFirstDeque() {
        if (firstDeque.isNotEmpty()) {
            firstDeque.removeLast()
            state = state.copy(enterKeyStatus = EnterKeyStatus.BACKSPACE)
        }
    }

    protected fun backSpaceOnSecondDeque() {
        if (secondDeque.isNotEmpty()) {
            secondDeque.removeLast()
            state = state.copy(enterKeyStatus = EnterKeyStatus.BACKSPACE)
        }
    }

    protected fun onExitPressed() {
        sendEvent(LogoutEvent())
    }

    abstract fun onBackPressed()

    abstract fun onNumberClicked(number: Int)

    abstract fun onBackSpaceClicked()

    protected abstract fun checkPins()

}