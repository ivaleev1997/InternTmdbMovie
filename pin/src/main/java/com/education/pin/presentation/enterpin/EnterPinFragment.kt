package com.education.pin.presentation.enterpin

import android.view.View
import androidx.fragment.app.viewModels
import com.education.core_api.presentation.fragment.BaseFragment
import com.education.pin.R

class EnterPinFragment : BaseFragment(R.layout.enter_pin_fragment) {

    companion object {
        fun newInstance() = EnterPinFragment()
    }

    private val viewModel: EnterPinViewModel by viewModels()

    override fun initViewElements(view: View) {

    }
}
