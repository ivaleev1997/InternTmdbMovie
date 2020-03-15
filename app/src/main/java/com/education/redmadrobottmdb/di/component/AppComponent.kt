package com.education.redmadrobottmdb.di.component

import android.app.Application
import android.content.Context
import com.education.core_api.mediator.SharedProvider
import com.education.redmadrobottmdb.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class]
)
interface AppComponent : SharedProvider {

    companion object {
        private var appComponent: AppComponent? = null

        fun create(application: Application): AppComponent {
            return appComponent
                ?:
            DaggerAppComponent.builder()
                .application(application)
                .build()
        }
    }

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(context: Context): Builder

        fun build(): AppComponent
    }
}