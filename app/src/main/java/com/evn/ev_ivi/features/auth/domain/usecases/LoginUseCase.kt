package com.evn.ev_ivi.features.auth.domain.usecases

import com.evn.ev_ivi.features.auth.domain.repository.LoginRepository

class LoginUseCase(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<Unit> {
        return try {
            loginRepository.login(username, password)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}