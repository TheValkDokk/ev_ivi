package com.evn.ev_ivi.features.auth.data.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("user_id")
    val userid: String,
    val password: String
)