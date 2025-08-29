package com.evn.ev_ivi.features.auth.domain.usecases

import com.evn.ev_ivi.core.common.domain.repository.AuthRepository
import com.evn.ev_ivi.features.auth.domain.repository.LoginRepository

class LoginUseCase(
    private val loginRepository: LoginRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<Unit> {
        return try {
            val token = loginRepository.login(username, password)
            authRepository.setToken(token.getOrThrow());
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}