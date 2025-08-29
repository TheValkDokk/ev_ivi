package com.evn.ev_ivi.core.common.domain.usecase

import com.evn.ev_ivi.core.common.domain.repository.AuthRepository

class IsLoggedInUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Boolean {
        return authRepository.isLoggedIn()
    }
}