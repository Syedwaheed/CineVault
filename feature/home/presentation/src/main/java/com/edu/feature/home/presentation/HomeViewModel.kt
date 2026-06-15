package com.edu.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.core.domain.movie.SyncStatus
import com.edu.core.presentation.designsystem.components.SyncState
import com.edu.core.presentation.ui.UiText
import com.edu.feature.home.domain.GetTopRatedMoviesUseCase
import com.edu.feature.home.domain.GetTrendingMoviesUseCase
import com.edu.feature.home.domain.SyncMoviesUseCase
import com.edu.core.domain.util.NoParams
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getTrendingMovies: GetTrendingMoviesUseCase,
    private val getTopRatedMovies: GetTopRatedMoviesUseCase,
    private val syncMovies: SyncMoviesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<HomeEffect>()
    val uiEffect: SharedFlow<HomeEffect> = _uiEffect.asSharedFlow()

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)

    init {
        observeContent()
        triggerSync()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.Retry -> triggerSync()
            is HomeAction.MovieClicked -> viewModelScope.launch {
                _uiEffect.emit(HomeEffect.NavigateToDetail(action.movieId))
            }
            HomeAction.SearchClicked -> viewModelScope.launch {
                _uiEffect.emit(HomeEffect.NavigateToSearch)
            }
        }
    }

    private fun observeContent() {
        viewModelScope.launch {
            combine(
                getTrendingMovies(NoParams),
                getTopRatedMovies(NoParams),
                _syncState,
            ) { trending, topRated, syncState ->
                HomeUiState.Success(
                    trendingMovies = trending,
                    topRatedMovies = topRated,
                    syncState = syncState,
                ) as HomeUiState
            }
                .catch { e ->
                    emit(HomeUiState.Error(UiText.DynamicString(e.message ?: "Unknown error")))
                }
                .collect { _uiState.value = it }
        }
    }

    private fun triggerSync() {
        viewModelScope.launch {
            syncMovies(NoParams).collect { status ->
                _syncState.value = when (status) {
                    SyncStatus.Idle -> SyncState.Idle
                    SyncStatus.Syncing -> SyncState.Syncing
                    SyncStatus.Success -> SyncState.Idle
                    is SyncStatus.Error -> SyncState.Error
                }
            }
        }
    }
}
