package com.education.pin.presentation.createpin

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
import javax.inject.Inject
import javax.inject.Named

class CreatePinFragment : PinFragment(R.layout.create_pin_fragment) {

    companion object {
        fun newInstance() =
            CreatePinFragment()
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    @Named("PinViewModel")
    internal lateinit var viewModelTrigger: ViewModelTrigger

    private val viewModel: CreatePinViewModel by viewModels {
        viewModelFactory
    }

    private val args: CreatePinFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        PinComponent.create((requireActivity().application as AppWithComponent).getComponent()).inject(this)
        super.onAttach(context)
    }

    override fun initViewElements(view: View) {
        viewModel.appContext = requireActivity().applicationContext
        viewModel.userCredentials = args.userCredentials
        viewModel.biometricSecurity = BiometricSecurity(true, this, ContextCompat.getMainExecutor(context))
        viewModel.checkBiometric()

        setupToolBar()

        pinKeyboard.setOnNumberClickedListener(::onNumberClicked)
        pinKeyboard.setOnBackSpaceClickListener(::onBackspaceClicked)

        observe(viewModel.liveState, ::renderViewState)
        observe(viewModel.eventsQueue, ::onEvent)
    }

    private fun onEvent(event: Event) {
        onFragmentEvent(event, createPinFragmentContainer)
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
            EnterKeyStatus.REPEAT -> {
                hiddenPin.clear()
                createPinTitleTextView.makeInvisible()
                createPinTitleTextView.setText(R.string.repeat_pin_code)
                createPinTitleTextView.makeVisible()
            }
            EnterKeyStatus.CLEAN -> {
                hiddenPin.clear()
                wrongEnterTextView.makeInvisible()
                if (!viewModel.isSecondDeque) {
                    createPinTitleTextView.makeInvisible()
                    createPinTitleTextView.setText(R.string.come_up_with_pin_code)
                    createPinTitleTextView.makeVisible()
                }
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

    private fun setupToolBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).title = " "

        toolbar.setNavigationOnClickListener {
            viewModel.onBackPressed()
        }
    }
}
