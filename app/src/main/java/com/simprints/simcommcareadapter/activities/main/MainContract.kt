package com.simprints.simcommcareadapter.activities.main

import androidx.lifecycle.MutableLiveData
import com.simprints.libsimprints.Identification
import com.simprints.libsimprints.Registration
import com.simprints.libsimprints.Verification
import com.simprints.simcommcareadapter.BaseView
import com.simprints.simcommcareadapter.activities.main.MainViewModel.ReturnIdentification
import com.simprints.simcommcareadapter.events.DataEvent
import com.simprints.simcommcareadapter.events.Event


interface MainContract {

    interface View : BaseView<ViewModel>

    interface ViewModel : com.simprints.simcommcareadapter.ViewModel {

        val requestRegisterCallout: MutableLiveData<Event>
        val requestIdentifyCallout: MutableLiveData<Event>
        val requestVerifyCallout: MutableLiveData<Event>
        val requestConfirmIdentityCallout: MutableLiveData<Event>
        val returnActionErrorToClient: MutableLiveData<Event>

        val returnRegistration: MutableLiveData<DataEvent<Registration>>
        val returnIdentification: MutableLiveData<DataEvent<ReturnIdentification>>
        val returnVerification: MutableLiveData<DataEvent<Verification>>

        fun start(action: String?)

        fun processRegistration(registration: Registration)

        fun processIdentification(identifications: ArrayList<Identification>, sessionId: String)

        fun processVerification(verification: Verification)

        fun processReturnError()

    }

}