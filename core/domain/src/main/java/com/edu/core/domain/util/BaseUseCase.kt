package com.edu.core.domain.util

import kotlinx.coroutines.flow.Flow

fun interface FlowUseCase<in P, out T> {
    operator fun invoke(params: P): Flow<T>
}

fun interface SuspendUseCase<in P, out T> {
    suspend operator fun invoke(params: P): T
}

object NoParams
