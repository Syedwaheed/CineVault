package com.edu.auth.presentation.login

sealed interface LoginEffect {
    data class OpenBrowser(val url: String) : LoginEffect
    data object NavigateToHome : LoginEffect
}
