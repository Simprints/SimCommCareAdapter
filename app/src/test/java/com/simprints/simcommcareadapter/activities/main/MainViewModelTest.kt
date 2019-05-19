package com.simprints.simcommcareadapter.activities.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.simprints.libsimprints.Identification
import com.simprints.libsimprints.Registration
import com.simprints.libsimprints.Tier
import com.simprints.libsimprints.Verification
import com.simprints.simcommcareadapter.activities.main.MainViewModel.Companion.ACTION_CONFIRM_IDENTITY
import com.simprints.simcommcareadapter.activities.main.MainViewModel.Companion.ACTION_IDENTIFY
import com.simprints.simcommcareadapter.activities.main.MainViewModel.Companion.ACTION_REGISTER
import com.simprints.simcommcareadapter.activities.main.MainViewModel.Companion.ACTION_VERIFY
import com.simprints.simcommcareadapter.activities.main.MainViewModel.ReturnIdentification
import com.simprints.simcommcareadapter.events.DataEventObserver
import com.simprints.simcommcareadapter.events.Event
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.isA
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    companion object {
        private const val SINGLE_INVOCATION = 1
    }

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var eventObserver: Observer<Event>

    @Test
    fun startPresenterForRegister_ShouldRequestRegister() {
        MainViewModel().apply {
            start(ACTION_REGISTER)
            requestRegisterCallout.observeForever(eventObserver)
        }
        verify(eventObserver, Mockito.times(SINGLE_INVOCATION)).onChanged(isA(Event::class.java))
    }

    @Test
    fun startPresenterForIdentify_ShouldRequestIdentify() {
        MainViewModel().apply {
            start(ACTION_IDENTIFY)
            requestIdentifyCallout.observeForever(eventObserver)
        }
        verify(eventObserver, Mockito.times(SINGLE_INVOCATION)).onChanged(isA(Event::class.java))
    }

    @Test
    fun startPresenterForVerify_ShouldRequestVerify() {
        MainViewModel().apply {
            start(ACTION_VERIFY)
            requestVerifyCallout.observeForever(eventObserver)
        }
        verify(eventObserver, Mockito.times(SINGLE_INVOCATION)).onChanged(isA(Event::class.java))
    }

    @Test
    fun startPresenterWithGarbage_ShouldReturnActionError() {
        MainViewModel().apply {
            start(ACTION_CONFIRM_IDENTITY)
            requestConfirmIdentityCallout.observeForever(eventObserver)
        }
        verify(eventObserver, Mockito.times(SINGLE_INVOCATION)).onChanged(isA(Event::class.java))
    }

    @Test
    fun processRegistration_ShouldReturnValidOdkRegistration() {
        val registration = Registration(UUID.randomUUID().toString())
        val ob: DataEventObserver<Registration> = Mockito.spy(DataEventObserver {
            assert(it.guid == registration.guid)
        })

        MainViewModel().apply {
            processRegistration(registration)
            returnRegistration.observeForever(ob)
        }
    }

    @Test
    fun processIdentification_ShouldReturnValidOdkIdentification() {
        val id1 = Identification(UUID.randomUUID().toString(), 100, Tier.TIER_1)
        val id2 = Identification(UUID.randomUUID().toString(), 15, Tier.TIER_5)
        val sessionId = UUID.randomUUID().toString()

        val ob: DataEventObserver<ReturnIdentification> = Mockito.spy(DataEventObserver {
            assert(it.identifications[0].guid == id1.guid)
            assert(it.identifications[0].confidence == id1.confidence)
            assert(it.identifications[0].tier == id1.tier)

            assert(it.identifications[1].guid == id2.guid)
            assert(it.identifications[1].confidence == id2.confidence)
            assert(it.identifications[1].tier == id2.tier)

            assert(it.sessionId == sessionId)
        })

        MainViewModel().apply {
            processIdentification(arrayListOf(id1, id2), sessionId)
            returnIdentification.observeForever(ob)
        }
    }

    @Test
    fun processVerification_ShouldReturnValidOdkVerification() {
        val verification = Verification(100, Tier.TIER_1, UUID.randomUUID().toString())

        val ob: DataEventObserver<Verification> = Mockito.spy(DataEventObserver {
            assert(it.guid == verification.guid)
            assert(it.confidence == verification.confidence)
            assert(it.tier == verification.tier)
        })

        MainViewModel().apply {
            processVerification(verification)
            returnVerification.observeForever(ob)
        }
    }

    @Test
    fun processReturnError_ShouldCallActionError() {
        MainViewModel().apply {
            start("")
            returnActionErrorToClient.observeForever(eventObserver)
        }
        verify(eventObserver, Mockito.times(SINGLE_INVOCATION)).onChanged(isA(Event::class.java))
    }

    @Test
    fun startPresenterForConfirmIdentity_ShouldRequestConfirmIdentity() {
        MainViewModel().apply {
            start(ACTION_CONFIRM_IDENTITY)
            requestConfirmIdentityCallout.observeForever(eventObserver)
        }
        verify(eventObserver, Mockito.times(SINGLE_INVOCATION)).onChanged(isA(Event::class.java))
    }

}