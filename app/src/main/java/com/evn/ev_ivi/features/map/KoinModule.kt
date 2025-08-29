package com.evn.ev_ivi.features.map

import com.evn.ev_ivi.core.services.NetworkService
import com.evn.ev_ivi.features.map.data.datasource.remote.MapSearchDatasource
import com.evn.ev_ivi.features.map.data.datasource.remote.MapSearchDatasourceImpl
import com.evn.ev_ivi.features.map.data.repositories.MapRepositoryImpl
import com.evn.ev_ivi.features.map.domain.repositories.MapRepository
import com.evn.ev_ivi.features.map.domain.usecases.SearchLocationsUseCase
import com.evn.ev_ivi.features.map.presentation.viewmodels.MapPanelViewModel
import com.evn.ev_ivi.features.map.presentation.viewmodels.SearchPanelViewModelL
import com.evn.ev_ivi.features.map.presentation.viewmodels.SpeechToTextViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mapPanelModules = module {
    viewModel { SpeechToTextViewModel(androidContext()) }
    viewModel { MapPanelViewModel(androidContext()) }
    viewModel { SearchPanelViewModelL(get()) }
    
    single<MapSearchDatasource> { MapSearchDatasourceImpl(get()) }
    single<MapRepository> { MapRepositoryImpl(get()) }
    single { SearchLocationsUseCase(get()) }
    single { NetworkService.mapApi(get()) }
}