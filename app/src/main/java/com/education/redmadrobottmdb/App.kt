package com.education.redmadrobottmdb

import android.app.Application
import com.education.core_api.mediator.AppWithComponent
import com.education.core_api.mediator.CoreProvider
import com.education.redmadrobottmdb.di.AppComponent

class App : Application(), AppWithComponent {

    companion object {
        var appComponent: AppComponent? = null
    }

    override fun getComponent(): CoreProvider {
        return appComponent
            ?: AppComponent.init()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = (getComponent() as AppComponent)
    }
}
