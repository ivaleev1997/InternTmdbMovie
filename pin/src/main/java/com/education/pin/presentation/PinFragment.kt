package com.education.pin.presentation

import com.education.core_api.presentation.fragment.BaseFragment
import com.education.pin.domain.entity.PinViewState

abstract class PinFragment(layoutId: Int) : BaseFragment(layoutId) {

    protected abstract fun renderViewState(pinViewState: PinViewState)

    protected abstract fun onNumberClicked(number: Int)

    protected abstract fun onBackspaceClicked()
}
