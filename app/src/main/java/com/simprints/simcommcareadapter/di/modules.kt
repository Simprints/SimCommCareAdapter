package com.simprints.simcommcareadapter.di

import com.simprints.simcommcareadapter.activities.main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val koinModules = module {

    viewModel { MainViewModel() }

}