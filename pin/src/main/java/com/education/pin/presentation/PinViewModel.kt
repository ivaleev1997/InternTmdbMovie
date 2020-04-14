package com.education.pin.presentation

import androidx.lifecycle.MutableLiveData
import com.education.core_api.extension.delegate
import com.education.core_api.presentation.uievent.LogoutEvent
import com.education.core_api.presentation.viewmodel.BaseViewModel
import com.education.pin.domain.entity.EnterKeyStatus
import com.education.pin.domain.entity.Number
import com.education.pin.domain.entity.PinViewState
import com.education.pin.presentation.item.BackSpaceItem
import com.education.pin.presentation.item.ExitItem
import com.education.pin.presentation.item.NumberItem
import com.xwray.groupie.kotlinandroidextensions.Item
import java.util.*
import kotlin.collections.HashMap

abstract class PinViewModel : BaseViewModel() {
    companion object {
        const val PIN_NUMBERS_COUNT_4 = 4
        const val KEYBOARD_NUMBERS_9 = 9
    }

    val liveState = MutableLiveData(PinViewState(number = Number.ZERO))
    protected var state: PinViewState by liveState.delegate()

    var isSecondDeque = false
        protected set

    protected val firstDeque: Deque<Int> = LinkedList<Int>()
    protected val secondDeque: Deque<Int> = LinkedList<Int>()

    protected val dequeSizeMapToNumber: HashMap<Int, Number> = HashMap<Int,Number>().apply {
        put(0, Number.ZERO)
        put(1, Number.FIRST)
        put(2, Number.SECOND)
        put(3, Number.THIRD)
        put(4, Number.FOURTH)
    }

    fun genKeyboardItems(withExit: Boolean): List<Item> {
        val listNumbers = MutableList(KEYBOARD_NUMBERS_9) {
            NumberItem(it + 1, ::onNumberClicked) as Item
        }.apply {
            if (withExit)
                add(ExitItem(::onExitPressed))
            else
                add(NumberItem())
            add(NumberItem(0, ::onNumberClicked))
            add(BackSpaceItem(::onBackSpaceClicked))
        }

        return listNumbers
    }

    protected fun withSecondDeque(number: Int) {
        if (secondDeque.size < PIN_NUMBERS_COUNT_4) {
            secondDeque.push(number)
            state = state.copy(
                enterKeyStatus = EnterKeyStatus.ENTER,
                number = dequeSizeMapToNumber[secondDeque.size]
            )
            if (secondDeque.size == PIN_NUMBERS_COUNT_4) {
                checkPins()
            }
        }
    }

    protected fun backSpaceOnFirstDeque() {
        if (firstDeque.isNotEmpty()) {
            firstDeque.removeLast()
            state = state.copy(enterKeyStatus = EnterKeyStatus.BACKSPACE, number = dequeSizeMapToNumber[firstDeque.size + 1])
        }
    }

    protected fun backSpaceOnSecondDeque() {
        if (secondDeque.isNotEmpty()) {
            secondDeque.removeLast()
            state = state.copy(enterKeyStatus = EnterKeyStatus.BACKSPACE, number = dequeSizeMapToNumber[secondDeque.size + 1])
        }
    }

    protected fun onExitPressed() {
        sendEvent(LogoutEvent())
    }

    abstract fun onBackPressed()

    protected abstract fun onNumberClicked(number: Int?)

    protected abstract fun onBackSpaceClicked()

    protected abstract fun checkPins()

}