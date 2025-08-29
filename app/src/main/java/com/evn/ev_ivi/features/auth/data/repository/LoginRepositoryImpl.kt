package com.evn.ev_ivi.features.auth.data.repository

import android.content.SharedPreferences
import com.evn.ev_ivi.features.auth.data.datasource.remote.AuthRemoteDatasource
import com.evn.ev_ivi.features.auth.domain.repository.LoginRepository

class LoginRepositoryImpl(
    private val authRemoteDatasource: AuthRemoteDatasource,
) : LoginRepository {

    override suspend fun login(username: String, password: String): Result<String> {
        return try {
            val response = authRemoteDatasource.login(username, password)
            val token = response.token
            if (token.isNotEmpty()) {
                Result.success(token)
            } else {
                Result.failure(Exception("Invalid token"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}