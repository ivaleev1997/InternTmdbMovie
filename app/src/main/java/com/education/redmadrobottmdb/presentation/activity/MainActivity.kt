package com.education.redmadrobottmdb.presentation.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.education.core_api.RootNavigationGraphDirections
import com.education.core_api.di.AppWithComponent
import com.education.core_api.presentation.activity.BaseActivity
import com.education.core_api.presentation.ui.LoginMediator
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.login.presentation.LoginFragmentDirections
import com.education.redmadrobottmdb.R
import com.education.redmadrobottmdb.di.component.MainComponent
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity(), BaseActivity {

    @Inject
    lateinit var viewModelTrigger: ViewModelTrigger
    @Inject
    lateinit var appViewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var loginMediator: LoginMediator

    lateinit var rootNavController: NavController

    private val viewModel: MainViewModel by viewModels {
        appViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainComponent.create((application as AppWithComponent).getComponent()).inject(this)
        setContentView(R.layout.activity_main)

        setupRootNavComponent()

        if (!viewModel.isSessionExist())
            startLoginScreen()
        else {
            startMainAppScreen()
        }
    }

    private fun setupRootNavComponent() {
        rootNavController = findNavController(R.id.nav_host_fragment)
        //supportActionBar?.hide()
    }

    override fun startMainAppScreen() {
        Timber.d("startMainAppScreen")
        navigateTo(RootNavigationGraphDirections.toStartGraph())
    }

    override fun startLoginScreen() {
        rootNavController.navigate(LoginFragmentDirections.toLoginFragment())
    }

    override fun logout() {
        viewModel.onLogoutClicked()
        startLoginScreen()
    }

    override fun navigateTo(navDirection: NavDirections) {
        rootNavController.navigate(navDirection)
    }
}
