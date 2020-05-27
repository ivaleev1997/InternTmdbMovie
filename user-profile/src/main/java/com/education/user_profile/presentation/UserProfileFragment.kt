package com.education.user_profile.presentation

import android.content.Context
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.education.core_api.di.AppWithComponent
import com.education.core_api.extension.observe
import com.education.core_api.presentation.fragment.BaseFragment
import com.education.core_api.presentation.uievent.Event
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.user_profile.R
import com.education.user_profile.di.ProfileComponent
import com.education.user_profile.domain.entity.UserProfileViewState
import kotlinx.android.synthetic.main.user_profile_fragment.*
import javax.inject.Inject

class UserProfileFragment : BaseFragment(R.layout.user_profile_fragment) {

    companion object {
        fun newInstance() =
            UserProfileFragment()
    }
    @Inject
    internal lateinit var appViewModelFactory: ViewModelProvider.Factory
    @Inject
    internal lateinit var viewModelTrigger: ViewModelTrigger

    private val viewModel: UserProfileViewModel by viewModels {
        appViewModelFactory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ProfileComponent.create((requireActivity().application as AppWithComponent).getComponent())
            .inject(this)
    }

    override fun initViewElements(view: View) {
        viewModel.loadUserProfile()
        logoutButton.setOnClickListener {
            logout()
        }

        observe(viewModel.liveState, ::renderState)
        observe(viewModel.eventsQueue, ::onEvent)
    }

    private fun onEvent(event: Event) {
        onFragmentEvent(event, userProfileConstraintLayout, logoutButton)
    }

    private fun renderState(state: UserProfileViewState) {
        userName.text = state.userName
    }
}
