package com.simprints.simcommcareadapter

import android.app.Application
import com.simprints.simcommcareadapter.di.koinModules
import org.koin.android.ext.android.startKoin


class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(androidContext = this, modules = listOf(koinModules))
    }

}