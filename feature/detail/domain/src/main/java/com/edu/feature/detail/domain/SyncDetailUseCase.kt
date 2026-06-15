package com.edu.feature.detail.domain

import com.edu.core.domain.util.DataError
import com.edu.core.domain.util.EmptyResult
import com.edu.core.domain.util.SuspendUseCase

class SyncDetailUseCase(
    private val remoteDataSource: DetailRemoteDataSource,
) : SuspendUseCase<Int, EmptyResult<DataError.NetworkError>> {
    override suspend fun invoke(params: Int): EmptyResult<DataError.NetworkError> =
        remoteDataSource.syncMovieDetail(params)
}