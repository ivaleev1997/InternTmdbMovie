package com.education.redmadrobottmdb

import android.app.Application
import com.education.core_api.mediator.AppWithFacade
import com.education.core_api.mediator.ProvidersFacade
import com.education.redmadrobottmdb.di.FacadeComponent

class App : Application(), AppWithFacade {

    companion object {
        var facade: FacadeComponent? = null
    }

    override fun getFacade(): ProvidersFacade {
        return facade
            ?: FacadeComponent.init()
    }

    override fun onCreate() {
        super.onCreate()
        facade = (getFacade() as FacadeComponent)
    }
}