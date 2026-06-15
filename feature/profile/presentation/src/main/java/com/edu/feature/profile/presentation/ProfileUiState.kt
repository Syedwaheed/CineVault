package com.edu.feature.profile.presentation

import com.edu.core.domain.auth.Account
import com.edu.core.domain.auth.UserStats
import com.edu.core.presentation.ui.UiText

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Success(
        val account: Account,
        val stats: UserStats,
    ) : ProfileUiState
    data class Error(val message: UiText) : ProfileUiState
}
