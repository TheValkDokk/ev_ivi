package com.evn.ev_ivi.core.di

import com.evn.ev_ivi.core.common.data.repositories.UserRepositoryImpl
import com.evn.ev_ivi.core.common.domain.repositories.UserRepository
import com.evn.ev_ivi.core.navigation.navigationModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    singleOf(::UserRepositoryImpl) bind UserRepository::class
}