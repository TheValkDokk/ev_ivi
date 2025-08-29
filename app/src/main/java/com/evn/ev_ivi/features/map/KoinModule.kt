package com.evn.ev_ivi.features.map

import com.evn.ev_ivi.features.map.presentation.viewmodels.MapPanelViewModel
import com.evn.ev_ivi.features.map.presentation.viewmodels.SpeechToTextViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mapPanelModules = module {
    viewModel { SpeechToTextViewModel(androidContext()) }
    viewModel { MapPanelViewModel() }
}