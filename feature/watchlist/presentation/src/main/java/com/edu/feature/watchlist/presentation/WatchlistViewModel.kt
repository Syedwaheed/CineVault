package com.edu.feature.watchlist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.core.domain.movie.Movie
import com.edu.core.domain.util.NoParams
import com.edu.core.presentation.ui.UiText
import com.edu.feature.watchlist.domain.AddToWatchlistUseCase
import com.edu.feature.watchlist.domain.GetWatchlistUseCase
import com.edu.feature.watchlist.domain.RemoveFromWatchlistUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WatchlistViewModel(
    private val getWatchlist: GetWatchlistUseCase,
    private val removeFromWatchlist: RemoveFromWatchlistUseCase,
    private val addToWatchlist: AddToWatchlistUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<WatchlistUiState>(WatchlistUiState.Loading)
    val uiState: StateFlow<WatchlistUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<WatchlistEffect>()
    val uiEffect: SharedFlow<WatchlistEffect> = _uiEffect.asSharedFlow()

    private var pendingRemoval: Movie? = null

    init {
        observeWatchlist()
    }

    fun onAction(action: WatchlistAction) {
        when (action) {
            is WatchlistAction.MovieClicked -> viewModelScope.launch {
                _uiEffect.emit(WatchlistEffect.NavigateToDetail(action.movieId))
            }
            is WatchlistAction.RemoveMovie -> {
                pendingRemoval = action.movie
                viewModelScope.launch {
                    removeFromWatchlist(action.movie.id)
                    _uiEffect.emit(WatchlistEffect.ShowUndoSnackbar(action.movie))
                }
            }
            WatchlistAction.UndoRemove -> {
                val movie = pendingRemoval ?: return
                pendingRemoval = null
                viewModelScope.launch {
                    addToWatchlist(movie)
                }
            }
            WatchlistAction.BrowseMovies -> viewModelScope.launch {
                _uiEffect.emit(WatchlistEffect.BrowseMovies)
            }
        }
    }

    private fun observeWatchlist() {
        viewModelScope.launch {
            getWatchlist(NoParams)
                .catch { e ->
                    _uiState.value = WatchlistUiState.Error(
                        UiText.DynamicString(e.message ?: "Something went wrong")
                    )
                }
                .collect { movies ->
                    _uiState.value = if (movies.isEmpty()) WatchlistUiState.Empty
                                     else WatchlistUiState.Success(movies)
                }
        }
    }
}
