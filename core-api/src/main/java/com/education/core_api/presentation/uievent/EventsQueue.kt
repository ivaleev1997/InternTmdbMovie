package com.education.core_api.presentation.uievent

import androidx.lifecycle.MutableLiveData
import java.util.*

class EventsQueue : MutableLiveData<Queue<Event>>() {

    fun offer(event: Event) {
        val queue = (value ?: LinkedList()).apply { add(event) }
        value = queue
    }
}
