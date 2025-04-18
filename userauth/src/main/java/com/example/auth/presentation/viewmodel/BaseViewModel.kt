package com.example.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Base authentication view model with common functionality
 */

abstract class BaseAuthViewModel : ViewModel() {
    // UI State for authentication screens
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val isLoggedIn = MutableStateFlow(false)
    protected fun setLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    protected fun setAuthStatus(status: AuthStatus) {
        _uiState.update { it.copy(authStatus = status) }
    }
    protected fun setEmailStatus(isEmailVerified: Boolean) {
        _uiState.update { it.copy(isEmailVerified = isEmailVerified) }
    }

    protected fun setErrorMessage(message: String?) {
        _uiState.update { it.copy(errorMessage = message) }
    }

    protected fun resetState() {
        _uiState.update {
            AuthUiState()
        }
    }


}

/**
 * Status of authentication operations
 */
enum class AuthStatus {
    IDLE,
    SUCCESS,
    ERROR
}
/**
 * Common UI state for authentication screens
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val isEmailVerified: Boolean = false,
    val errorMessage: String? = null,
    val authStatus: AuthStatus = AuthStatus.IDLE
)