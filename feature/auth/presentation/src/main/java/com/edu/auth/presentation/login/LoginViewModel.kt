package com.edu.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.auth.domain.usecase.GetAuthorizationUrlUseCase
import com.edu.auth.domain.usecase.GetRequestTokenUseCase
import com.edu.auth.domain.usecase.LoginUseCase
import com.edu.core.domain.util.NoParams
import com.edu.core.domain.util.Result
import com.edu.core.presentation.ui.asUiText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val getRequestToken: GetRequestTokenUseCase,
    private val getAuthorizationUrl: GetAuthorizationUrlUseCase,
    private val login: LoginUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<LoginEffect>()
    val uiEffect: SharedFlow<LoginEffect> = _uiEffect.asSharedFlow()

    private var pendingRequestToken: String? = null

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.ContinueWithTmdb -> fetchTokenAndOpenBrowser()
            LoginAction.ApprovalConfirmed -> completeLogin()
            LoginAction.Cancel -> {
                pendingRequestToken = null
                _uiState.value = LoginUiState.Idle
            }
            LoginAction.RetryClicked -> _uiState.value = LoginUiState.Idle
        }
    }

    private fun fetchTokenAndOpenBrowser() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.FetchingToken
            when (val result = getRequestToken(NoParams)) {
                is Result.Success -> {
                    pendingRequestToken = result.data
                    val url = getAuthorizationUrl(result.data)
                    _uiState.value = LoginUiState.WaitingForApproval
                    _uiEffect.emit(LoginEffect.OpenBrowser(url))
                }
                is Result.Error -> {
                    _uiState.value = LoginUiState.Error(result.error.asUiText())
                }
            }
        }
    }

    private fun completeLogin() {
        val token = pendingRequestToken ?: return
        viewModelScope.launch {
            _uiState.value = LoginUiState.LoggingIn
            when (val result = login(token)) {
                is Result.Success -> _uiEffect.emit(LoginEffect.NavigateToHome)
                is Result.Error -> {
                    _uiState.value = LoginUiState.Error(result.error.asUiText())
                }
            }
        }
    }
}
