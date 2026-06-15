package com.edu.feature.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.core.domain.movie.Movie
import com.edu.core.presentation.ui.UiText
import com.edu.feature.search.domain.SearchMoviesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchMovies: SearchMoviesUseCase,
) : ViewModel() {

    val genres: List<String> = listOf("All", "Drama", "Sci-Fi", "Action", "Comedy", "Mystery")

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedGenre = MutableStateFlow("All")
    val selectedGenre: StateFlow<String> = _selectedGenre.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<SearchEffect>()
    val uiEffect: SharedFlow<SearchEffect> = _uiEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            combine(_query, _selectedGenre) { q, genre -> q to genre }
                .debounce(DEBOUNCE_MS)
                .flatMapLatest { (query, genre) ->
                    if (query.isBlank()) {
                        flowOf(SearchUiState.Idle)
                    } else {
                        searchMovies(query)
                            .map { movies ->
                                SearchUiState.Success(
                                    query = query,
                                    results = filterByGenre(movies, genre),
                                    selectedGenre = genre,
                                    isSearching = false,
                                ) as SearchUiState
                            }
                            .onStart { emit(SearchUiState.Loading(query, genre)) }
                            .catch { e ->
                                emit(
                                    SearchUiState.Error(
                                        query = query,
                                        selectedGenre = genre,
                                        message = UiText.DynamicString(e.message ?: "Unknown error"),
                                    )
                                )
                            }
                    }
                }
                .collect { _uiState.value = it }
        }
    }

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.QueryChanged -> _query.value = action.query
            is SearchAction.GenreSelected -> _selectedGenre.value = action.genre
            is SearchAction.MovieClicked -> viewModelScope.launch {
                _uiEffect.emit(SearchEffect.NavigateToDetail(action.movieId))
            }
            SearchAction.ClearQuery -> {
                _query.value = ""
                _selectedGenre.value = "All"
                _uiState.value = SearchUiState.Idle
            }
        }
    }

    private fun filterByGenre(movies: List<Movie>, genre: String): List<Movie> {
        if (genre == "All") return movies
        val genreId = GENRE_NAME_TO_ID[genre] ?: return movies
        return movies.filter { genreId in it.genreIds }
    }

    private companion object {
        const val DEBOUNCE_MS = 300L
        val GENRE_NAME_TO_ID = mapOf(
            "Drama" to 18,
            "Sci-Fi" to 878,
            "Action" to 28,
            "Comedy" to 35,
            "Mystery" to 9648,
        )
    }
}
