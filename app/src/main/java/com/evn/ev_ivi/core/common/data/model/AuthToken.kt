package com.evn.ev_ivi.core.common.data.model

data class AuthTokenResponseModel (
    val token: String,
    val userType: String
)

data class AuthTokenRequestModel (
    val username: String,
    val password: String
)