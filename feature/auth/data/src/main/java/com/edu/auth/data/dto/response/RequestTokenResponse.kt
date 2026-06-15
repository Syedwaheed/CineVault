package com.edu.auth.data.dto.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RequestTokenResponse(
    @SerialName("expires_at")
    val expiresAt: String,
    @SerialName("request_token")
    val requestToken: String,
    @SerialName("success")
    val success: Boolean
)