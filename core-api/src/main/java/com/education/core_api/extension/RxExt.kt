package com.education.core_api.extension

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun Completable.schedulersIoToMain(provider: SchedulersProvider): Completable {
    return subscribeOn(provider.io()).observeOn(provider.mainThread())
}

fun <T> Single<T>.schedulersIoToMain(provider: SchedulersProvider): Single<T> {
    return subscribeOn(provider.io()).observeOn(provider.mainThread())
}

fun <T> Flowable<T>.schedulersComputationToMain(provider: SchedulersProvider): Flowable<T> {
    return subscribeOn(provider.computation()).observeOn(provider.mainThread())
}
fun <T : Any> Single<T>.flatMapCompletableAction(action: (T) -> Unit): Completable {
    return flatMapCompletable { param -> Completable.fromAction { action.invoke(param) } }
}

interface SchedulersProvider {
    fun io(): Scheduler

    fun mainThread(): Scheduler

    fun computation(): Scheduler
}

object SchedulersProviderImpl : SchedulersProvider {
    override fun io(): Scheduler = Schedulers.io()

    override fun mainThread(): Scheduler = AndroidSchedulers.mainThread()

    override fun computation(): Scheduler = Schedulers.computation()
}
