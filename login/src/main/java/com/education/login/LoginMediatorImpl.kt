package com.education.login

import androidx.fragment.app.FragmentManager
import com.education.core_api.ui.LoginMediator
import javax.inject.Inject

class LoginMediatorImpl
    @Inject constructor() : LoginMediator {
    override fun startLoginScreen(containerId: Int, fragmentManager: FragmentManager) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(containerId, LoginFragment.newInstance())
        transaction.commit()
    }
}
