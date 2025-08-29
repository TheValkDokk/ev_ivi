package com.evn.ev_ivi.features.auth.data.datasource.remote

import com.evn.ev_ivi.features.auth.data.models.AuthTokenResponse
import com.evn.ev_ivi.features.auth.data.models.LoginRequest

interface AuthRemoteDatasource {
    suspend fun login(username: String, password: String): AuthTokenResponse
}

class AuthRemoteDatasourceImpl(
    private val authApi: AuthApi
) : AuthRemoteDatasource {
    override suspend fun login(
        username: String,
        password: String
    ): AuthTokenResponse {
        return try {
            val request = LoginRequest(username, password)
            authApi.login(request)
        } catch (e: Exception) {
            throw e;
        }
    }
}