package com.evn.ev_ivi.core.utils

import android.app.Application
import android.content.SharedPreferences

fun getSharedPrefs(androidApplication: Application): SharedPreferences {
    return  androidApplication.getSharedPreferences("default",  android.content.Context.MODE_PRIVATE)
}
