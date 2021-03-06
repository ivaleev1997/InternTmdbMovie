package com.education.redmadrobottmdb.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.education.core_api.RootNavigationGraphDirections
import com.education.core_api.di.AppWithComponent
import com.education.core_api.extension.makeVisible
import com.education.core_api.extension.observe
import com.education.core_api.presentation.activity.BaseActivity
import com.education.core_api.presentation.ui.LoginMediator
import com.education.core_api.presentation.uievent.Event
import com.education.core_api.presentation.uievent.NavigateToEvent
import com.education.core_api.presentation.viewmodel.ViewModelTrigger
import com.education.login.presentation.LoginFragmentDirections
import com.education.redmadrobottmdb.R
import com.education.redmadrobottmdb.di.component.MainComponent
import com.education.redmadrobottmdb.domain.MainActivityViewState
import com.education.redmadrobottmdb.domain.RootStatus
import com.education.util.WifiSecurityChecker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity(), BaseActivity {

    @Inject
    internal lateinit var viewModelTrigger: ViewModelTrigger
    @Inject
    internal lateinit var appViewModelFactory: ViewModelProvider.Factory
    @Inject
    internal lateinit var loginMediator: LoginMediator

    lateinit var rootNavController: NavController

    private val viewModel: MainViewModel by viewModels {
        appViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainComponent.create((application as AppWithComponent).getComponent()).inject(this)
        setContentView(R.layout.activity_main)

        viewModel.onCreate(application)

        observe(viewModel.liveState, ::renderViewState)
        observe(viewModel.eventsQueue, ::onEvent)
    }

    private fun onEvent(event: Event) {
        when (event) {
            is NavigateToEvent -> rootNavController.navigate(event.navDirections)
        }
    }

    private fun renderViewState(mainActivityViewState: MainActivityViewState) {
        when (mainActivityViewState.rootStatus) {
            RootStatus.ROOTED -> {
                root_alert.makeVisible()
            }
            RootStatus.NOT_ROOTED -> {
                setupRootNavComponent()
                viewModel.defineScreen()
            }
        }
    }

    private fun setupRootNavComponent() {
        rootNavController = findNavController(R.id.nav_host_fragment)
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

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
        setupWifiConnectivityListener()
    }

    private fun setupWifiConnectivityListener() {
        WifiSecurityChecker.setupListener(applicationContext) {
            Timber.d("wifi triggered")
            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.attention_wifi))
                .setMessage(resources.getString(R.string.wifi_alert_describe))
                .setPositiveButton(resources.getString(R.string.wifi_alert_positive_button)) { dialog, which ->
                    dialog.dismiss()
                }
                .setNegativeButton(resources.getString(R.string.wifi_alert_settings_button)) { dialog, which ->
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }
                .show()
        }
    }
}
