package com.edu.auth.presentation.login

import com.edu.core.presentation.ui.UiText

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object FetchingToken : LoginUiState
    data object WaitingForApproval : LoginUiState
    data object LoggingIn : LoginUiState
    data class Error(val message: UiText) : LoginUiState
}
