package com.education.core_api.crashlytics

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class FirebaseCrashlyticsTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        if (priority == Log.ERROR || priority == Log.DEBUG) {
            val crashlytics = FirebaseCrashlytics.getInstance()
            crashlytics.log(message)
            if (throwable != null) {
                crashlytics.recordException(throwable)
            }
        } else return
    }
}