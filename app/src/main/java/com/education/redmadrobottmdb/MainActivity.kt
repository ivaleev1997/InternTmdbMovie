package com.education.redmadrobottmdb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.education.core_api.mediator.AppWithFacade
import com.education.core_api.ui.LoginMediator
import com.education.redmadrobottmdb.di.MainComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var loginMediator: LoginMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainComponent.create((application as AppWithFacade).getFacade()).inject(this)
        setContentView(R.layout.activity_main)

        loginMediator.startLoginScreen(R.id.fragment_container, supportFragmentManager)
    }
}
