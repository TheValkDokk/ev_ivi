package com.evn.ev_ivi.features.auth.data.repository

import android.content.SharedPreferences
import com.evn.ev_ivi.features.auth.data.datasource.remote.AuthRemoteDatasource
import com.evn.ev_ivi.features.auth.domain.repository.LoginRepository

class LoginRepositoryImpl(
    private val authRemoteDatasource: AuthRemoteDatasource,
    private val sharedPreferences: SharedPreferences
) : LoginRepository {
    
    override suspend fun login(username: String, password: String): Result<Unit> {
        return try {
            val response = authRemoteDatasource.login(username, password)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}