package com.edu.feature.search.presentation

sealed interface SearchAction {
    data class QueryChanged(val query: String) : SearchAction
    data class GenreSelected(val genre: String) : SearchAction
    data class MovieClicked(val movieId: Int) : SearchAction
    data object ClearQuery : SearchAction
}
