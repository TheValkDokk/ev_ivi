package com.evn.ev_ivi.features.auth.domain.repository

interface LoginRepository {
    suspend fun login(username: String, password: String): Result<String>
}