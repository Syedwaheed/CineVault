package com.edu.feature.detail.domain

import com.edu.core.domain.util.DataError
import com.edu.core.domain.util.EmptyResult
import com.edu.core.domain.util.Result

interface DetailRemoteDataSource {
    suspend fun syncMovieDetail(movieId: Int): EmptyResult<DataError.NetworkError>
    suspend fun fetchTrailerKey(movieId: Int): Result<String?, DataError.NetworkError>
}