package com.simprints.simcommcareadapter.activities.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import com.simprints.libsimprints.Constants.*
import com.simprints.libsimprints.Identification
import com.simprints.libsimprints.Registration
import com.simprints.libsimprints.Verification
import com.simprints.simcommcareadapter.R.string.failed_confirmation
import com.simprints.simcommcareadapter.activities.main.MainViewModel.ReturnIdentification
import com.simprints.simcommcareadapter.events.DataEventObserver
import com.simprints.simcommcareadapter.events.EventObserver
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity(), MainContract.View {

    companion object {
        private const val REGISTER_REQUEST_CODE = 97
        private const val IDENTIFY_REQUEST_CODE = 98
        private const val VERIFY_REQUEST_CODE = 99
    }

    override val viewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.requestRegisterCallout.observe(this, EventObserver { requestRegisterCallout() })
        viewModel.requestIdentifyCallout.observe(this, EventObserver { requestIdentifyCallout() })
        viewModel.requestVerifyCallout.observe(this, EventObserver { requestVerifyCallout() })
        viewModel.requestConfirmIdentityCallout.observe(this, EventObserver { requestConfirmIdentityCallout() })
        viewModel.returnActionErrorToClient.observe(this, EventObserver { returnActionErrorToClient() })

        viewModel.returnRegistration.observe(this, DataEventObserver { returnRegistration(it) })
        viewModel.returnIdentification.observe(this, DataEventObserver { returnIdentification(it) })
        viewModel.returnVerification.observe(this, DataEventObserver { returnVerification(it) })

        viewModel.start(intent.action)
    }

    private fun returnActionErrorToClient() {
        setResult(SIMPRINTS_INVALID_INTENT_ACTION, intent)
        finish()
    }

    private fun requestRegisterCallout() {
        val registerIntent = Intent(SIMPRINTS_REGISTER_INTENT).apply { putExtras(intent) }
        startActivityForResult(registerIntent, REGISTER_REQUEST_CODE)
    }

    private fun requestIdentifyCallout() {
        val identifyIntent = Intent(SIMPRINTS_IDENTIFY_INTENT).apply { putExtras(intent) }
        startActivityForResult(identifyIntent, IDENTIFY_REQUEST_CODE)
    }

    private fun requestVerifyCallout() {
        val verifyIntent = Intent(SIMPRINTS_VERIFY_INTENT).apply { putExtras(intent) }
        startActivityForResult(verifyIntent, VERIFY_REQUEST_CODE)
    }

    private fun requestConfirmIdentityCallout() {
        try {
            Intent(SIMPRINTS_SELECT_GUID_INTENT).apply {
                putExtras(intent)
                setPackage(SIMPRINTS_PACKAGE_NAME)
                sendService(this)
            }
        } catch (ex: Exception) {
            Crashlytics.logException(ex)
            toast(getString(failed_confirmation))
        }
        setResult(RESULT_OK)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK || data == null)
            setResult(resultCode, data).also { finish() }
        else
            when (requestCode) {
                REGISTER_REQUEST_CODE -> viewModel.processRegistration(
                    data.getParcelableExtra(SIMPRINTS_REGISTRATION)
                )
                IDENTIFY_REQUEST_CODE -> viewModel.processIdentification(
                    data.getParcelableArrayListExtra<Identification>(SIMPRINTS_IDENTIFICATIONS),
                    data.getStringExtra(SIMPRINTS_SESSION_ID)
                )
                VERIFY_REQUEST_CODE -> viewModel.processVerification(
                    data.getParcelableExtra(SIMPRINTS_VERIFICATION)
                )
                else -> viewModel.processReturnError()
            }
    }

    private fun returnRegistration(registration: Registration) = Intent().let {
        it.putExtra(SIMPRINTS_REGISTRATION, registration)
        sendOkResult(it)
    }

    private fun returnIdentification(identification: ReturnIdentification) = Intent().let {
        it.putExtra(SIMPRINTS_IDENTIFICATIONS, identification.identifications)
        it.putExtra(SIMPRINTS_SESSION_ID, identification.sessionId)
        sendOkResult(it)
    }

    private fun returnVerification(verification: Verification) = Intent().let {
        it.putExtra(SIMPRINTS_VERIFICATION, verification)
        sendOkResult(it)
    }

    private fun sendOkResult(intent: Intent) {
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun sendService(intent: Intent) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        startForegroundService(intent)
    else
        startService(intent)

}
