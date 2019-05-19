package com.simprints.simcommcareadapter.events


open class Event {

    var hasBeenHandled = false
        private set // Allow external read but not write

    fun getIfNotHandled() {
        hasBeenHandled = true
    }

}

