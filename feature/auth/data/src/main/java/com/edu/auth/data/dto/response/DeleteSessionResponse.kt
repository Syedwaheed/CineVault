package com.edu.auth.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteSessionResponse(
    @SerialName("success")
    val success: Boolean
)