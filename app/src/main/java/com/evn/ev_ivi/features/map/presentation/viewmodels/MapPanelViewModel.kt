package com.evn.ev_ivi.features.map.presentation.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.kakaomobility.knsdk.KNLanguageType
import com.kakaomobility.knsdk.KNSDK
import com.kakaomobility.knsdk.ui.view.KNNaviView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.internal.platform.PlatformRegistry.applicationContext

class MapPanelViewModel() : ViewModel() {
    private val _kakaoInit = MutableStateFlow(false)
    val kakaoInit = _kakaoInit.asStateFlow()

    init {
        KNSDK.apply {
            val mainApplication = applicationContext as Application
            install(mainApplication, "${mainApplication.filesDir}")
            val appKey = "958d9749a2e1f35ef2982b0b2b1a09a5"
            initializeWithAppKey(
                appKey,
                "1.0.0",
                "test-user",
                KNLanguageType.KNLanguageType_KOREAN
            ) { error ->
                if (error == null) {
                    _kakaoInit.value = true
                }
            }
        }
    }
}