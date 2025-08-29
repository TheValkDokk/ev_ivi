package com.evn.ev_ivi.core.di

import android.content.SharedPreferences
import com.evn.ev_ivi.core.common.data.datasouce.local.AuthLocalDatasource
import com.evn.ev_ivi.core.common.data.datasouce.local.AuthLocalDatasourceImpl
import com.evn.ev_ivi.core.common.data.repository.AuthRepositoryImpl
import com.evn.ev_ivi.core.common.domain.repository.AuthRepository
import com.evn.ev_ivi.core.common.domain.usecase.GetTokenUseCase
import com.evn.ev_ivi.core.common.domain.usecase.IsLoggedInUseCase
import com.evn.ev_ivi.core.common.domain.usecase.LogoutUseCase
import com.evn.ev_ivi.core.common.domain.usecase.SetTokenUseCase
import com.evn.ev_ivi.core.utils.getSharedPrefs
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single {
        getSharedPrefs(androidApplication())
    }

    single<SharedPreferences.Editor> {
        getSharedPrefs(androidApplication()).edit()
    }

    // Data sources
    single<AuthLocalDatasource> {
        AuthLocalDatasourceImpl(get())
    }

    // Repositories
    single<AuthRepository> {
        AuthRepositoryImpl(get())
    }

    // Use cases
    single { IsLoggedInUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { SetTokenUseCase(get()) }
    single { GetTokenUseCase(get()) }
}