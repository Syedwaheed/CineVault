package com.edu.feature.profile.domain

import com.edu.core.domain.auth.ProfileRepository
import com.edu.core.domain.util.NoParams
import com.edu.core.domain.util.SuspendUseCase

class LogoutProfileUseCase(
    private val repository: ProfileRepository,
) : SuspendUseCase<NoParams, Unit> {
    override suspend fun invoke(params: NoParams) = repository.logout()
}
