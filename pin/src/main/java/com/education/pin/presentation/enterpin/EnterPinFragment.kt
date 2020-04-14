package com.education.pin.presentation.enterpin

import android.content.Context
import android.view.View
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
import com.education.pin.di.PinComponent
import com.education.pin.domain.entity.EnterKeyStatus
import com.education.pin.domain.entity.PinViewState
import com.education.pin.presentation.PinFragment
import kotlinx.android.synthetic.main.create_pin_fragment.wrongEnterTextView
import kotlinx.android.synthetic.main.enter_pin_fragment.*
import kotlinx.android.synthetic.main.pin_keyboard.*
import javax.inject.Inject
import javax.inject.Named

class EnterPinFragment : PinFragment(R.layout.enter_pin_fragment) {

    companion object {
        fun newInstance() = EnterPinFragment()
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    @Named("EnterPinViewModel")
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
        initRecyclerView(pinRecyclerView)

        updateAdapter(viewModel.genKeyboardItems(true))

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
                handleEnterKeyStatusEnter(pinViewState.number)
            }
            EnterKeyStatus.BACKSPACE -> {
                wrongEnterTextView.makeInvisible()
                handleBackspaceKeyStatus(pinViewState.number)
            }
            EnterKeyStatus.ERROR -> {
                wrongEnterTextView.makeVisible()
                setAllHidePinIconsError()
            }
        }
    }
}
