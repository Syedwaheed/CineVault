package com.edu.feature.profile.domain

import com.edu.core.domain.auth.Account
import com.edu.core.domain.auth.ProfileRepository
import com.edu.core.domain.util.FlowUseCase
import com.edu.core.domain.util.NoParams
import kotlinx.coroutines.flow.Flow

class GetProfileUseCase(
    private val repository: ProfileRepository,
) : FlowUseCase<NoParams, Account> {
    override fun invoke(params: NoParams): Flow<Account> =
        repository.getProfile()
}
