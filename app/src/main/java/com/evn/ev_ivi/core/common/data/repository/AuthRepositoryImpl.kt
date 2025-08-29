package com.evn.ev_ivi.core.common.data.repository

import com.evn.ev_ivi.core.common.data.datasouce.local.AuthLocalDatasource
import com.evn.ev_ivi.core.common.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val localDatasource: AuthLocalDatasource
) : AuthRepository {
    
    override fun isLoggedIn(): Boolean {
        return localDatasource.isLoggedIn()
    }
    
    override fun logout() {
        localDatasource.logout()
    }
    
    override fun setToken(token: String) {
        localDatasource.setToken(token)
    }
    
    override fun getToken(): String? {
        return localDatasource.getToken()
    }
}