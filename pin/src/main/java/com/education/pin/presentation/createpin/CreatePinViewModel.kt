package com.education.pin.presentation.createpin

import android.content.Context
import com.education.core_api.dto.UserCredentials
import com.education.core_api.extension.SchedulersProvider
import com.education.core_api.extension.schedulersComputationToMain
import com.education.core_api.presentation.uievent.NavigateToEvent
import com.education.core_api.presentation.uievent.TryLaterEvent
import com.education.pin.domain.PinUseCase
import com.education.pin.domain.entity.EnterKeyStatus
import com.education.pin.presentation.PinViewModel
import timber.log.Timber


class CreatePinViewModel(
    private val useCase: PinUseCase,
    private val schedulersProvider: SchedulersProvider
) : PinViewModel() {

    lateinit var userCredentials: UserCredentials
    lateinit var appContext: Context

    override fun onBackPressed() {
        if (!isSecondDeque)
            onExitPressed()
        else {
            state = state.copy(enterKeyStatus = EnterKeyStatus.CLEAN)
            firstDeque.clear()
            secondDeque.clear()
            isSecondDeque = false
        }
    }

    override fun onNumberClicked(number: Int?) {
        if (number != null) {
            if (!isSecondDeque) {
                withFirstDeque(number)
            } else {
                withSecondDeque(number)
            }
        }
    }

    override fun checkPins() {
        if (firstDeque == secondDeque) {
            val pin = firstDeque.joinToString(separator = "") { it.toString() }
            Timber.d("Pins are same! pin: $pin")

            useCase.saveUserCredentials(userCredentials, pin)
                .andThen(useCase.saveUserName(appContext))
                .schedulersComputationToMain(schedulersProvider)
                .subscribe({
                    Timber.d("Success encryption")
                    sendEvent(NavigateToEvent(CreatePinFragmentDirections.createPinToStartGraph()))
                },{
                    error ->
                    Timber.e(error)
                    sendEvent(TryLaterEvent())

                }).autoDispose()
        } else {
            Timber.d("Pins are different")
            state = state.copy(enterKeyStatus = EnterKeyStatus.ERROR)
        }
    }

    private fun withFirstDeque(number: Int) {
        if (firstDeque.size < PIN_NUMBERS_COUNT_4) {
            firstDeque.push(number)
            state = state.copy(
                enterKeyStatus = EnterKeyStatus.ENTER,
                number = dequeSizeMapToNumber[firstDeque.size]
            )
            if (firstDeque.size == PIN_NUMBERS_COUNT_4) {
                Timber.d("All numbers in first clicked")
                isSecondDeque = true
                state = state.copy(enterKeyStatus = EnterKeyStatus.REPEAT)
            }
        }
    }

    override fun onBackSpaceClicked() {
        if (!isSecondDeque) {
            backSpaceOnFirstDeque()
        } else {
            backSpaceOnSecondDeque()
        }
    }
}
