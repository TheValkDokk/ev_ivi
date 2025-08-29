package com.evn.ev_ivi.core.di

import android.content.SharedPreferences
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
}