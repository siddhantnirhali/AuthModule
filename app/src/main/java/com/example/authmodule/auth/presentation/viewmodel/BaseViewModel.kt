package com.example.authmodule.auth.presentation.viewmodel

import android.util.Log
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
    protected val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    protected fun setLoading(isLoading: Boolean) {
        //_uiState.value = _uiState.value.copy(isLoading = isLoading)
        _uiState.update { it.copy(isLoading = isLoading) }

        Log.d("BaseAuthViewModel", "uiState-> isLoading -> ${uiState.value.isLoading}")
    }

    protected fun setAuthStatus(status: AuthStatus) {
        _uiState.update { it.copy(authStatus = status) }
        Log.d("BaseAuthViewModel", "uiState-> isLoading -> ${uiState.value.authStatus}")
    }
    protected fun setEmailStatus(isEmailVerified: Boolean) {
        _uiState.update { it.copy(isEmailVerified = isEmailVerified) }
        Log.d("BaseAuthViewModel", "uiState-> isEmailVerified -> ${uiState.value.isEmailVerified}")
    }

    protected fun setErrorMessage(message: String?) {
        _uiState.update { it.copy(errorMessage = message) }
    }

    protected fun resetState() {
        _uiState.update {
            AuthUiState()
        }
    }

    // Authentication methods to be implemented by child classes
    abstract fun authenticateWithGoogle()
    abstract fun authenticateWithFacebook()
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