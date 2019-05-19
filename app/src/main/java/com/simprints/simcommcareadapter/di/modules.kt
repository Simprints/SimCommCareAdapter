package com.simprints.simcommcareadapter.di

import com.simprints.simcommcareadapter.activities.main.MainViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


val koinModules = module {

    viewModel { MainViewModel() }

}