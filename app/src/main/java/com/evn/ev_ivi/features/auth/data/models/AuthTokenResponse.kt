package com.evn.ev_ivi.features.auth.data.models

import com.google.gson.annotations.SerializedName

data class AuthTokenResponse (
    val token: String,
    @SerializedName("user_type")
    val userType: String
)