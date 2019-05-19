package com.simprints.simcommcareadapter.events


import androidx.lifecycle.Observer

/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been handled.
 */
open class EventObserver(private val onEventUnhandledContent: () -> Unit) : Observer<Event> {
    override fun onChanged(event: Event?) {
        event?.getIfNotHandled()?.let {
            onEventUnhandledContent()
        }
    }
}