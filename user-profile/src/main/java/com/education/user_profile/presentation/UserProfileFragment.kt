package com.education.user_profile.presentation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.education.core_api.presentation.fragment.BaseFragment
import com.education.user_profile.R


class UserProfileFragment : BaseFragment(R.layout.user_profile_fragment) {

    companion object {
        fun newInstance() =
            UserProfileFragment()
    }

    private lateinit var viewModel: UserProfileViewModel

    override fun initViewElements(view: View) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
