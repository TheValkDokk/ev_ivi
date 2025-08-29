package com.evn.ev_ivi

import android.app.Application
import com.evn.ev_ivi.core.di.appModule
import com.evn.ev_ivi.features.map.mapPanelModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            val modulesList = listOf(appModule, mapPanelModules)
            modules(modulesList)
        }
    }
}