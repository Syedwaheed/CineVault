package com.edu.feature.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.core.domain.movie.WatchlistRepository
import com.edu.core.presentation.ui.asUiText
import com.edu.feature.detail.domain.GetCreditsUseCase
import com.edu.feature.detail.domain.GetMovieDetailUseCase
import com.edu.feature.detail.domain.GetSimilarMoviesUseCase
import com.edu.feature.detail.domain.SyncDetailUseCase
import com.edu.feature.detail.domain.DetailRemoteDataSource
import com.edu.core.domain.util.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailViewModel(
    private val movieId: Int,
    private val getMovieDetail: GetMovieDetailUseCase,
    private val getCredits: GetCreditsUseCase,
    private val getSimilarMovies: GetSimilarMoviesUseCase,
    private val syncDetail: SyncDetailUseCase,
    private val remoteDataSource: DetailRemoteDataSource,
    private val watchlistRepository: WatchlistRepository,
) : ViewModel() {

    private val _isCastLoading = MutableStateFlow(true)

    val uiState: StateFlow<DetailUiState> = combine(
        getMovieDetail(movieId),
        getCredits(movieId),
        getSimilarMovies(movieId),
        _isCastLoading,
        watchlistRepository.isInWatchlist(movieId),
    ) { movie, cast, similar, castLoading, isInWatchlist ->
        if (movie == null) {
            DetailUiState.Loading
        } else {
            DetailUiState.Success(
                movie = movie,
                cast = cast,
                similarMovies = similar,
                isCastLoading = castLoading && cast.isEmpty(),
                isInWatchlist = isInWatchlist,
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DetailUiState.Loading,
    )

    private val _uiEffect = MutableSharedFlow<DetailEffect>()
    val uiEffect: SharedFlow<DetailEffect> = _uiEffect.asSharedFlow()

    init {
        triggerSync()
    }

    fun onAction(action: DetailAction) {
        when (action) {
            DetailAction.Back -> emitEffect(DetailEffect.NavigateBack)
            is DetailAction.SimilarMovieClicked -> emitEffect(DetailEffect.NavigateToDetail(action.movieId))
            DetailAction.ToggleWatchlist -> toggleWatchlist()
            DetailAction.WatchTrailer -> fetchAndPlayTrailer()
        }
    }

    private fun toggleWatchlist() {
        val state = uiState.value as? DetailUiState.Success ?: return
        viewModelScope.launch {
            if (state.isInWatchlist) {
                watchlistRepository.removeFromWatchlist(state.movie.id)
            } else {
                watchlistRepository.addToWatchlist(state.movie)
            }
        }
    }

    private fun fetchAndPlayTrailer() {
        viewModelScope.launch {
            when (val result = remoteDataSource.fetchTrailerKey(movieId)) {
                is Result.Success -> {
                    val key = result.data
                    if (key != null) {
                        _uiEffect.emit(DetailEffect.PlayTrailer(key))
                    } else {
                        _uiEffect.emit(DetailEffect.ShowError(
                            com.edu.core.presentation.ui.UiText.DynamicString("No trailer available for this movie")
                        ))
                    }
                }
                is Result.Error -> _uiEffect.emit(DetailEffect.ShowError(result.error.asUiText()))
            }
        }
    }

    private fun triggerSync() {
        viewModelScope.launch {
            _isCastLoading.value = true
            when (val result = syncDetail(movieId)) {
                is Result.Error -> {
                    _isCastLoading.value = false
                    if (uiState.value is DetailUiState.Loading) {
                        _uiEffect.emit(DetailEffect.ShowError(result.error.asUiText()))
                    }
                }
                is Result.Success -> _isCastLoading.value = false
            }
        }
    }

    private fun emitEffect(effect: DetailEffect) {
        viewModelScope.launch { _uiEffect.emit(effect) }
    }
}
