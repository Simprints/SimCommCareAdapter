package com.simprints.simcommcareadapter.events


import androidx.lifecycle.Observer

/**
 * An [Observer] for [DataEvent]s, simplifying the pattern of checking if the [DataEvent]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [DataEvent]'s contents has not been handled.
 */
open class DataEventObserver<T>(private val onEventUnhandledContent: (T) -> Unit)
    : Observer<DataEvent<T>> {
    override fun onChanged(dataEvent: DataEvent<T>?) {
        dataEvent?.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}
