package com.education.core_api.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.education.core_api.presentation.uievent.Event
import com.education.core_api.presentation.uievent.EventsQueue
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

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
}
