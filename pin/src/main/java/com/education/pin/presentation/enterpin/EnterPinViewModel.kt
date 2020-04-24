package com.education.pin.presentation.enterpin

import android.content.Context
import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.extension.mapDistinct
import com.education.core_api.extension.schedulersComputationToMain
import com.education.core_api.presentation.uievent.LogoutEvent
import com.education.core_api.presentation.uievent.NavigateToEvent
import com.education.pin.biometric.BiometricAuthStatus
import com.education.pin.domain.PinUseCase
import com.education.pin.domain.entity.EnterKeyStatus
import com.education.pin.presentation.PinViewModel
import timber.log.Timber

class EnterPinViewModel(
    private val useCase: PinUseCase,
    private val schedulersProvider: SchedulersProvider
) : PinViewModel() {

    init {
        isSecondDeque = true
    }

    fun checkBiometricAuth(context: Context) {
        checkBiometric(context)
        if (isBiometricEnable) {
            biometricSecurity.authCallback = { biometricResult ->
                if (biometricResult.biometricAuthStatus == BiometricAuthStatus.SUCCESS && !biometricResult.masterPinKey.isNullOrEmpty()) {
                    getUserLoginAsCheckValidPin(biometricResult.masterPinKey)
                }
            }
            useCase.getMasterKeyPin()
                .schedulersComputationToMain(schedulersProvider)
                .subscribe({ encryptedPin ->
                    biometricSecurity.authenticate(encryptedPin)
                }, { error ->
                    Timber.d(error)
                }).autoDispose()
        }
    }

    var userName = liveState.mapDistinct { it.userName }

    fun setUserName(userName: String) {
        state = state.copy(userName = userName)
    }

    override fun onBackPressed() {
        sendEvent(LogoutEvent())
    }

    override fun onNumberClicked(number: Int) {
        withSecondDeque(number)
    }

    override fun onBackSpaceClicked() {
        if (!wasError)
            backSpaceOnSecondDeque()
        else {
            clearDeques(secondDeque)
            wasError = false
        }
    }

    override fun checkPins() {
        Timber.d("check pins")
        getUserLoginAsCheckValidPin(secondDeque.joinToString(separator = "") { it.toString() })
    }

    private fun getUserLoginAsCheckValidPin(pin: String) {
        useCase.getUserLogin(pin)
            .schedulersComputationToMain(schedulersProvider)
            .subscribe({userLogin ->
                if (userLogin.isNotEmpty())
                    sendEvent(NavigateToEvent(EnterPinFragmentDirections.enterPinToStartGraph()))
                else {
                    state = state.copy(enterKeyStatus = EnterKeyStatus.ERROR)
                    wasError = true
                }
            }, { error ->
                Timber.e(error)
                state = state.copy(enterKeyStatus = EnterKeyStatus.ERROR)
                wasError = true
            })
            .autoDispose()
    }
}
