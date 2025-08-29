package com.evn.ev_ivi.features.auth

import com.evn.ev_ivi.core.services.NetworkService
import com.evn.ev_ivi.features.auth.data.datasource.remote.AuthRemoteDatasource
import com.evn.ev_ivi.features.auth.data.datasource.remote.AuthRemoteDatasourceImpl
import com.evn.ev_ivi.features.auth.data.repository.LoginRepositoryImpl
import com.evn.ev_ivi.features.auth.domain.repository.LoginRepository
import com.evn.ev_ivi.features.auth.domain.usecases.LoginUseCase
import com.evn.ev_ivi.features.auth.presentation.viewmodels.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single<AuthRemoteDatasource> { AuthRemoteDatasourceImpl(get()) }
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    single { LoginUseCase(get(),get()) }
    viewModel { LoginViewModel(get()) }
    single { NetworkService.authApi(get()) }
}