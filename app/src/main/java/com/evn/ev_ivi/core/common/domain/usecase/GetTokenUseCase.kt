package com.evn.ev_ivi.core.common.domain.usecase

import com.evn.ev_ivi.core.common.domain.repository.AuthRepository

class GetTokenUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): String? {
        return authRepository.getToken()
    }
}