package com.edu.auth.domain.usecase

import com.edu.core.domain.auth.SessionStorage

class IsLoggedInUseCase(
    private val sessionStorage: SessionStorage,
) {
    operator fun invoke(): Boolean = sessionStorage.isLoggedIn()
}
