package com.edu.auth.presentation.login

sealed interface LoginAction {
    data object ContinueWithTmdb : LoginAction
    data object ApprovalConfirmed : LoginAction
    data object Cancel : LoginAction
    data object RetryClicked : LoginAction
}
