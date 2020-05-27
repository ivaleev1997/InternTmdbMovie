package com.education.core_api.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.education.core_api.data.network.exception.UnAuthorizedException
import com.education.core_api.extension.isNetworkException
import com.education.core_api.presentation.uievent.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {
    private val compositeDisposable by lazy { CompositeDisposable() }

    val eventsQueue = EventsQueue()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    protected fun sendEvent(event: Event) {
        eventsQueue.offer(event)
    }

    protected fun Disposable.autoDispose(): Disposable {
        compositeDisposable.add(this)

        return this
    }

    protected fun handleError(error: Throwable) {
        when {
            error.isNetworkException() -> sendEvent(
                NoNetworkEvent()
            )

            error is UnAuthorizedException -> sendEvent(
                UnAuthorizedEvent()
            )

            else -> sendEvent(
                TryLaterEvent()
            )
        }
        Timber.e(error)
    }
}
