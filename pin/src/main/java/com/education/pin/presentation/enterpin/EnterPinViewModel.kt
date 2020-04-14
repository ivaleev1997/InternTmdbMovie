package com.education.pin.presentation.enterpin

import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.extension.mapDistinct
import com.education.core_api.extension.schedulersComputationToMain
import com.education.core_api.presentation.uievent.LogoutEvent
import com.education.core_api.presentation.uievent.NavigateToEvent
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

    var userName = liveState.mapDistinct { it.userName }

    fun setUserName(userName: String) {
        state = state.copy(userName = userName)
    }

    override fun onBackPressed() {
        sendEvent(LogoutEvent())
    }

    override fun onNumberClicked(number: Int?) {
        if (number != null) withSecondDeque(number)
    }

    override fun onBackSpaceClicked() {
        backSpaceOnSecondDeque()
    }

    override fun checkPins() {
        Timber.d("check pins")
        useCase.getUserLogin(secondDeque.joinToString(separator = "") { it.toString() })
            .schedulersComputationToMain(schedulersProvider)
            .subscribe({userLogin ->
                if (userLogin.isNotEmpty())
                    sendEvent(NavigateToEvent(EnterPinFragmentDirections.enterPinToStartGraph()))
                else
                    state = state.copy(enterKeyStatus = EnterKeyStatus.ERROR)
            }, { error ->
                Timber.e(error)
                state = state.copy(enterKeyStatus = EnterKeyStatus.ERROR)
            })
            .autoDispose()
    }

}
