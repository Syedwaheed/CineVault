package com.edu.core.network

object ApiConstants {
    const val POSTER_W500 = "w500"
    const val BACKDROP_W780 = "w780"

    object Endpoints {
        const val MOVIE_POPULAR = "movie/popular"
        const val MOVIE_TRENDING = "trending/movie/day"
        const val MOVIE_TOP_RATED = "movie/top_rated"
        const val MOVIE_UPCOMING = "movie/upcoming"
        const val MOVIE_DETAIL = "movie/{movieId}"
        const val MOVIE_CREDITS = "movie/{movieId}/credits"
        const val MOVIE_SIMILAR = "movie/{movieId}/similar"
        const val MOVIE_VIDEOS = "movie/{movieId}/videos"
        const val SEARCH_MOVIE = "search/movie"
        const val AUTH_REQUEST_TOKEN = "authentication/token/new"
        const val AUTH_CREATE_SESSION = "authentication/session/new"
        const val AUTH_DELETE_SESSION = "authentication/session"
        const val ACCOUNT_DETAILS = "account"
        const val WATCHLIST_ADD = "account/{accountId}/watchlist"
        const val WATCHLIST_GET = "account/{accountId}/watchlist/movies"
    }
}
