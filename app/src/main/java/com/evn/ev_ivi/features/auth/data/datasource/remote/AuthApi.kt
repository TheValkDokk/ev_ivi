package com.evn.ev_ivi.features.auth.data.datasource.remote

import com.evn.ev_ivi.features.auth.data.models.AuthTokenResponse
import com.evn.ev_ivi.features.auth.data.models.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/token/")
    suspend fun login(@Body request: LoginRequest): AuthTokenResponse
}