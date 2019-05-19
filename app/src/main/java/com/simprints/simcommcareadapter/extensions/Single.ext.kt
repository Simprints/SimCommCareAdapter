package com.simprints.simcommcareadapter.extensions

import androidx.lifecycle.MutableLiveData
import com.simprints.simcommcareadapter.events.Event

fun MutableLiveData<Event>.set() {
    this.value = Event()
}

fun MutableLiveData<Event>.post() {
    this.postValue(Event())
}