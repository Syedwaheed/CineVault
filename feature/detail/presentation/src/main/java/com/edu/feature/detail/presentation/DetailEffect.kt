package com.edu.feature.detail.presentation

import com.edu.core.presentation.ui.UiText

sealed interface DetailEffect {
    data object NavigateBack : DetailEffect
    data class NavigateToDetail(val movieId: Int) : DetailEffect
    data object RequireAuth : DetailEffect
    data class ShowError(val message: UiText) : DetailEffect
    data class PlayTrailer(val youtubeKey: String) : DetailEffect
}