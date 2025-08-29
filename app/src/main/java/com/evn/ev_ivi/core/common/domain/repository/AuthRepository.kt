package com.evn.ev_ivi.core.common.domain.repository

interface AuthRepository {
    fun isLoggedIn(): Boolean
    fun logout(): Unit
    fun setToken(token: String): Unit
    fun getToken(): String?
}