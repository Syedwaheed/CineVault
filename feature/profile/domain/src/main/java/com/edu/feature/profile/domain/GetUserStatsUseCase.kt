package com.edu.feature.profile.domain

import com.edu.core.domain.auth.ProfileRepository
import com.edu.core.domain.auth.UserStats
import com.edu.core.domain.util.FlowUseCase
import com.edu.core.domain.util.NoParams
import kotlinx.coroutines.flow.Flow

class GetUserStatsUseCase(
    private val repository: ProfileRepository,
) : FlowUseCase<NoParams, UserStats> {
    override fun invoke(params: NoParams): Flow<UserStats> =
        repository.getUserStats()
}
