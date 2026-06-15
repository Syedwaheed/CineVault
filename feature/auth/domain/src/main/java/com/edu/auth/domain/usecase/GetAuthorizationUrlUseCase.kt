package com.edu.auth.domain.usecase

import com.edu.auth.domain.AuthDataSource

class GetAuthorizationUrlUseCase(
    private val authDataSource: AuthDataSource,
) {
    operator fun invoke(requestToken: String): String =
        authDataSource.getAuthorizationUrl(requestToken)
}
