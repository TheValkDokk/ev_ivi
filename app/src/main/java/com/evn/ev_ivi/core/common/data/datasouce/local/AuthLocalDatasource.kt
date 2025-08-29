package com.evn.ev_ivi.core.common.data.datasouce.local

import android.content.SharedPreferences
import androidx.core.content.edit

interface AuthLocalDatasource {
    fun isLoggedIn(): Boolean
    fun logout(): Unit
    fun setToken(token: String): Unit
    fun getToken(): String?
}

class AuthLocalDatasourceImpl(
    private val sharedPref: SharedPreferences
):AuthLocalDatasource {
    override fun isLoggedIn(): Boolean {
        return sharedPref.getString("auth_token", null) != null
    }

    override fun logout() {
        sharedPref.edit { remove("auth_token") }
    }

    override fun setToken(token: String) {
        sharedPref.edit { putString("auth_token", token) }
    }

    override fun getToken(): String? {
        return sharedPref.getString("auth_token", null)
    }
}