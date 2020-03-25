package com.education.redmadrobottmdb

import android.app.Application
import com.education.core_api.di.AppWithComponent
import com.education.core_api.di.CoreProvider
import com.education.redmadrobottmdb.di.component.CoreComponent
import timber.log.Timber

class App : Application(), AppWithComponent {

    companion object {
        var coreComponent: CoreComponent? = null
    }

    override fun getComponent(): CoreProvider {
        return coreComponent
            ?: CoreComponent.init(this)
    }

    override fun onCreate() {
        super.onCreate()
        initLogger()
        coreComponent = (getComponent() as CoreComponent)
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}
