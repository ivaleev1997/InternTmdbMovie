package com.education.pin.presentation.createpin

import androidx.lifecycle.MutableLiveData
import com.education.core_api.extension.delegate
import com.education.core_api.presentation.viewmodel.BaseViewModel
import com.education.pin.domain.entity.EnterKeyStatus
import com.education.pin.domain.entity.Number
import com.education.pin.domain.entity.PinViewState
import com.education.pin.presentation.item.BackSpaceItem
import com.education.pin.presentation.item.NumberItem
import com.xwray.groupie.kotlinandroidextensions.Item
import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap


class CreatePinViewModel : BaseViewModel() {

    companion object {
        const val NUMBERS_COUNT = 4
    }

    val liveState = MutableLiveData(PinViewState(number = Number.ZERO))
    private var state: PinViewState by liveState.delegate()
//    val enterStatus = liveState.mapDistinct { it.enterStatus }
//    val each

    var isSecondDeque = false
        private set

    private val firstDeque: Deque<Int> = LinkedList<Int>()
    private val secondDeque: Deque<Int> = LinkedList<Int>()

    private val dequeSizeMapToNumber: HashMap<Int, Number> = HashMap<Int,Number>().apply {
        put(0, Number.ZERO)
        put(1, Number.FIRST)
        put(2, Number.SECOND)
        put(3, Number.THIRD)
        put(4, Number.FOURTH)
    }

    fun genKeyboardItems(count: Int): List<Item> {
        val listNumbers = MutableList(count) {
            NumberItem(it + 1, ::onNumberClicked) as Item
        }.apply {
            add(NumberItem())
            add(NumberItem(0, ::onNumberClicked))
            add(BackSpaceItem(::onBackSpaceClicked))
        }

        return listNumbers
    }

    fun onBackNavigatePressed() {
        state = state.copy(enterKeyStatus = EnterKeyStatus.CLEAN)
        firstDeque.clear()
        secondDeque.clear()
        isSecondDeque = false
    }

    private fun onNumberClicked(number: Int?) {
        if (number != null) {
            if (!isSecondDeque) {
                firstDeque.push(number)
                state = state.copy(
                    enterKeyStatus = EnterKeyStatus.ENTER,
                    number = dequeSizeMapToNumber[firstDeque.size]
                )
                if (firstDeque.size == NUMBERS_COUNT) {
                    Timber.d("All numbers in first clicked")
                    isSecondDeque = true
                    state = state.copy(enterKeyStatus = EnterKeyStatus.REPEAT)
                }
            } else {
                secondDeque.push(number)
                state = state.copy(
                    enterKeyStatus = EnterKeyStatus.ENTER,
                    number = dequeSizeMapToNumber[secondDeque.size]
                )
                if (secondDeque.size == NUMBERS_COUNT) {
                    checkPins()
                }
            }
        }
    }

    private fun onBackSpaceClicked() {
        if (!isSecondDeque) {
            if (firstDeque.isNotEmpty()) {
                firstDeque.removeLast()
                state = state.copy(enterKeyStatus = EnterKeyStatus.BACKSPACE, number = dequeSizeMapToNumber[firstDeque.size + 1])
            }
        } else {
            if (secondDeque.isNotEmpty()) {
                secondDeque.removeLast()
                state = state.copy(enterKeyStatus = EnterKeyStatus.BACKSPACE, number = dequeSizeMapToNumber[secondDeque.size + 1])
            }
        }
    }

    private fun checkPins() {
        if (firstDeque == secondDeque) {
            Timber.d("Pins are same!")
            // Save pin navigate to main screen
        } else {
            Timber.d("Pins are different")
            state = state.copy(enterKeyStatus = EnterKeyStatus.ERROR)
        }
    }
}
