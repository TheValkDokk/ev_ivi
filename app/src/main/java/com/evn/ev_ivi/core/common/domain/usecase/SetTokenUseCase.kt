package com.evn.ev_ivi.core.common.domain.usecase

import com.evn.ev_ivi.core.common.domain.repository.AuthRepository

class SetTokenUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(token: String) {
        authRepository.setToken(token)
    }
}