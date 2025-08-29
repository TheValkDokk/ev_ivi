package com.evn.ev_ivi

import android.app.Application
import com.evn.ev_ivi.core.di.appModule
import com.evn.ev_ivi.core.navigation.navigationModule
import com.evn.ev_ivi.features.auth.authModule
import com.evn.ev_ivi.features.map.mapPanelModules
import com.kakaomobility.knsdk.KNSDK
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication: Application() {
    companion object {
        lateinit var knsdk: KNSDK
        private const val TAG = "MainApplication"
    }
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            val modulesList = listOf(appModule, navigationModule, mapPanelModules, authModule)
            modules(modulesList)
        }
    }
}