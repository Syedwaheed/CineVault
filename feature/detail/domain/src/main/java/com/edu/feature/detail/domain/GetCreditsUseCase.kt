package com.edu.feature.detail.domain

import com.edu.core.domain.movie.Actor
import com.edu.core.domain.movie.MovieRepository
import com.edu.core.domain.util.FlowUseCase
import kotlinx.coroutines.flow.Flow

class GetCreditsUseCase(
    private val repository: MovieRepository,
) : FlowUseCase<Int, List<Actor>> {
    override fun invoke(params: Int): Flow<List<Actor>> =
        repository.getCredits(params)
}