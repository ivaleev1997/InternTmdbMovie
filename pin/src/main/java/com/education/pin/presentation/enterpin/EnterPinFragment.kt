package com.education.pin.presentation.enterpin

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.education.core_api.di.AppWithComponent
import com.education.core_api.extension.makeInvisible
import com.education.core_api.extension.makeVisible
import com.education.core_api.extension.observe
import com.education.core_api.presentation.uievent.Event
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.pin.R
import com.education.pin.biometric.BiometricSecurity
import com.education.pin.di.PinComponent
import com.education.pin.domain.entity.EnterKeyStatus
import com.education.pin.domain.entity.PinViewState
import com.education.pin.presentation.PinFragment
import kotlinx.android.synthetic.main.create_pin_fragment.*
import kotlinx.android.synthetic.main.create_pin_fragment.wrongEnterTextView
import kotlinx.android.synthetic.main.enter_pin_fragment.*
import kotlinx.android.synthetic.main.enter_pin_fragment.hiddenPin
import kotlinx.android.synthetic.main.enter_pin_fragment.pinKeyboard
import javax.inject.Inject

class EnterPinFragment : PinFragment(R.layout.enter_pin_fragment) {

    companion object {
        fun newInstance() = EnterPinFragment()
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var viewModelTrigger: ViewModelTrigger

    private val viewModel: EnterPinViewModel by viewModels {
        viewModelFactory
    }

    private val args: EnterPinFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        PinComponent.create((requireActivity().application as AppWithComponent).getComponent()).inject(this)
        super.onAttach(context)
    }

    override fun initViewElements(view: View) {
        viewModel.setUserName(args.userName)
        viewModel.biometricSecurity = BiometricSecurity(false, this, ContextCompat.getMainExecutor(context))
        viewModel.checkBiometricAuth(requireActivity().applicationContext)

        pinKeyboard.setOnNumberClickedListener(::onNumberClicked)
        pinKeyboard.setOnBackSpaceClickListener(::onBackspaceClicked)
        pinKeyboard.setOnExitClickListener(::onExitClicked)

        observe(viewModel.userName, ::renderUserNameState)
        observe(viewModel.liveState, ::renderViewState)
        observe(viewModel.eventsQueue, ::onEvent)
    }

    private fun onEvent(event: Event) {
        onFragmentEvent(event, enterPinFragmentContainer)
    }

    private fun renderUserNameState(userName: String?) {
        if (!userName.isNullOrEmpty())
            userNameTextView.text = userName
    }

    override fun renderViewState(pinViewState: PinViewState) {
        when(pinViewState.enterKeyStatus) {
            EnterKeyStatus.ENTER -> {
                hiddenPin.itemPressed()
            }
            EnterKeyStatus.BACKSPACE -> {
                hiddenPin.backspacePressed()
                wrongEnterTextView.makeInvisible()

            }
            EnterKeyStatus.CLEAN -> {
                hiddenPin.clear()
                wrongEnterTextView.makeInvisible()
            }
            EnterKeyStatus.ERROR -> {
                hiddenPin.wrongEntered()
                wrongEnterTextView.makeVisible()
            }
        }
    }

    override fun onNumberClicked(number: Int) {
        viewModel.onNumberClicked(number)
    }

    override fun onBackspaceClicked() {
        viewModel.onBackSpaceClicked()
    }

    private fun onExitClicked() {
        viewModel.onBackPressed()
    }
}
