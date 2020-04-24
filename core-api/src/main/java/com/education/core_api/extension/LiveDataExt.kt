package com.education.core_api.extension

import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.education.core_api.presentation.uievent.Event
import com.education.core_api.presentation.uievent.EventsQueue
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T, LD : LiveData<T>> Fragment.observe(liveData: LD, block: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner) { block(it) }
}

fun <T, LD : LiveData<T>> AppCompatActivity.observe(liveData: LD, block: (T) -> Unit) {
    liveData.observe(this) { block(it) }
}

fun <T> MutableLiveData<T>.onNext(next: T) {
    this.value = next
}

fun <T : Any> LiveData<T>.requireValue(): T = checkNotNull(value)

/**  * Делегат для работы с содержимым [MutableLiveData] как с полем.
 *
 * ```
 * val liveState = MutableLiveData<IntroViewState>(initialState)
 * var state: IntroViewState by liveState.delegate()
 * ```
 */
fun <T : Any> MutableLiveData<T>.delegate(): ReadWriteProperty<Any, T> {
    return object :
        ReadWriteProperty<Any, T> {
        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = onNext(value)
        override fun getValue(thisRef: Any, property: KProperty<*>): T = requireValue()
    }
}

fun Fragment.observe(eventsQueue: EventsQueue, eventHandler: (Event) -> Unit) {
    eventsQueue.observe(viewLifecycleOwner) { queue: Queue<Event>? ->
        while (queue != null && queue.isNotEmpty()) {
            eventHandler(queue.remove())
        }
    }
}

fun AppCompatActivity.observe(eventsQueue: EventsQueue, eventHandler: (Event) -> Unit) {
    eventsQueue.observe(this) { queue: Queue<Event>? ->
        while (queue != null && queue.isNotEmpty()) {
            eventHandler(queue.remove())
        }
    }
}

/** Последовательныйвызов[map]и[distinctUntilChanged], воднойфункции. */
inline fun <X, Y> LiveData<X>.mapDistinct(crossinline transform: (X) -> Y): LiveData<Y> {
    return map(transform).distinctUntilChanged()
}


@MainThread
inline fun <reified VM : ViewModel> Fragment.viewModels(
    noinline viewModelProducer: () -> VM
): Lazy<VM> {
    return lazy(LazyThreadSafetyMode.NONE) { createViewModel(viewModelProducer) }
}

@MainThread
inline fun <reified VM : ViewModel> Fragment.createViewModel(
    noinline viewModelProducer: () -> VM
): VM {
    val factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <VM : ViewModel> create(modelClass: Class<VM>) = viewModelProducer() as VM
    }
    return ViewModelProviders.of(this, factory).get(VM::class.java)
}