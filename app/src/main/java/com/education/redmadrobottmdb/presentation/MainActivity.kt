package com.education.redmadrobottmdb.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.education.core_api.presentation.activity.BaseActivity
import com.education.core_api.di.AppWithComponent
import com.education.core_api.presentation.ui.LoginMediator
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
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

    private val viewModel: MainViewModel by viewModels {
        appViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainComponent.create((application as AppWithComponent).getComponent()).inject(this)
        setContentView(R.layout.activity_main)
        if (!viewModel.isSessionExist())
            startLoginScreen()
        else {
            startMainAppScreen()
        }
    }

    override fun startMainAppScreen() {
        Timber.d("startMainAppScreen")
    }

    override fun startLoginScreen() {
        loginMediator.startLoginScreen(R.id.fragment_container, supportFragmentManager)
    }
}
