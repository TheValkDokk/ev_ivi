package com.evn.ev_ivi.core.common.data.datasource.remote

import com.evn.ev_ivi.core.common.data.model.AuthTokenRequestModel
import com.evn.ev_ivi.core.common.data.model.AuthTokenResponseModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RemoteDatasourceAPI {
    @POST("/api/auth/token")
    suspend fun auth(
        @Body request: AuthTokenRequestModel
    ): Response<AuthTokenResponseModel>
}

interface AuthRemoteDatasource {
    suspend fun auth(body: AuthTokenRequestModel): AuthTokenResponseModel?
}

class AuthRemoteDatasourceImpl(
    private val authApi: RemoteDatasourceAPI
) : AuthRemoteDatasource {
    override suspend fun auth(body: AuthTokenRequestModel): AuthTokenResponseModel? {
        val response = authApi.auth(body)

        if (response.isSuccessful) {
            print("DONE")
        } else {
            print("ERROR")
        }
        return null
    }
}