package com.simprints.simcommcareadapter.activities.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simprints.libsimprints.Identification
import com.simprints.libsimprints.Registration
import com.simprints.libsimprints.Verification
import com.simprints.simcommcareadapter.events.DataEvent
import com.simprints.simcommcareadapter.events.Event
import com.simprints.simcommcareadapter.extensions.post
import com.simprints.simcommcareadapter.extensions.set


class MainViewModel : ViewModel(), MainContract.ViewModel {

    companion object {
        private const val PACKAGE_NAME = "com.simprints.commcare"
        const val ACTION_REGISTER = "$PACKAGE_NAME.REGISTER"
        const val ACTION_IDENTIFY = "$PACKAGE_NAME.IDENTIFY"
        const val ACTION_VERIFY = "$PACKAGE_NAME.VERIFY"
        const val ACTION_CONFIRM_IDENTITY = "$PACKAGE_NAME.CONFIRM_IDENTITY"
    }

    override val requestRegisterCallout = MutableLiveData<Event>()
    override val requestIdentifyCallout = MutableLiveData<Event>()
    override val requestVerifyCallout = MutableLiveData<Event>()
    override val requestConfirmIdentityCallout = MutableLiveData<Event>()
    override val returnActionErrorToClient = MutableLiveData<Event>()

    override val returnRegistration = MutableLiveData<DataEvent<Registration>>()
    override val returnIdentification = MutableLiveData<DataEvent<ReturnIdentification>>()
    override val returnVerification = MutableLiveData<DataEvent<Verification>>()

    override fun start(action: String?) = when (action) {
        ACTION_REGISTER -> requestRegisterCallout.set()
        ACTION_IDENTIFY -> requestIdentifyCallout.set()
        ACTION_VERIFY -> requestVerifyCallout.set()
        ACTION_CONFIRM_IDENTITY -> requestConfirmIdentityCallout.post()
        else -> returnActionErrorToClient.set()
    }

    override fun processRegistration(registration: Registration) =
        returnRegistration.set(registration)

    override fun processIdentification(identifications: ArrayList<Identification>, sessionId: String) =
        returnIdentification.set(ReturnIdentification(identifications, sessionId))

    override fun processVerification(verification: Verification) =
        returnVerification.set(verification)

    override fun processReturnError() = returnActionErrorToClient.set()

    data class ReturnIdentification(val identifications: ArrayList<Identification>, val sessionId: String)

}