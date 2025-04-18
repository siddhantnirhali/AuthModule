package com.example.auth.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.auth.core.navigation.AuthNavigationProvider
import com.example.auth.core.navigation.Routes
import com.example.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * View model for the login screen
 */

class LoginViewModel(private val context: Context,
                     private val authRepository: AuthRepository,
                     private val navigationProvider: AuthNavigationProvider
) : BaseAuthViewModel() {
    // Login specific UI state
    private val _loginState = MutableStateFlow(LoginFormState())
    val loginState: StateFlow<LoginFormState> = _loginState.asStateFlow()

    // Update form values
    fun setEmail(value: String) {
        _loginState.update {
            it.copy(email = value)
        }
        validateEmail()
    }

    fun setPassword(value: String) {
        _loginState.update {
            it.copy(password = value)
        }
        validatePassword()
    }

    // Validation methods
    private fun validateEmail(): Boolean {
        val state = _loginState.value
        val emailValue = state.email

        return when {
            emailValue.isEmpty() -> {
                _loginState.update {
                    it.copy(emailError = "Email cannot be empty", isValidEmail = false)
                }
                false
            }

            !android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches() -> {
                _loginState.update {
                    it.copy(emailError = "Please enter a valid email address", isValidEmail = false)
                }
                false
            }

            else -> {
                _loginState.update {
                    it.copy(emailError = null, isValidEmail = true)
                }
                true
            }
        }
    }

    private fun validatePassword(): Boolean {
        val state = _loginState.value
        val passwordValue = state.password

        return when {
            passwordValue.isEmpty() -> {
                _loginState.update {
                    it.copy(passwordError = "Password cannot be empty", isValidPassword = false)
                }
                false
            }

            else -> {
                _loginState.update {
                    it.copy(passwordError = null, isValidPassword = true)
                }
                true
            }
        }
    }

    // Validate all form fields
    private fun validateForm(): Boolean {
        val isEmailValid = validateEmail()
        val isPasswordValid = validatePassword()
        return isEmailValid && isPasswordValid
    }

    // Login with email and password
    fun login() {
        if (!validateForm()) {
            return
        }

        setLoading(true)
        viewModelScope.launch {
            try {
                val state = _loginState.value
                val result = authRepository.loginWithEmailPassword(
                    email = state.email,
                    password = state.password
                )

                if (result.isSuccess) {
                    setAuthStatus(AuthStatus.SUCCESS)
                    Log.d("AuthStatus -> Login ->", "SUCCESS")

                } else {
                    Log.d("AuthStatus -> Login ->", "Login failed")
                    setErrorMessage(result.exceptionOrNull()?.message ?: "Login failed")
                    setAuthStatus(AuthStatus.ERROR)
                }
            } catch (e: Exception) {
                setErrorMessage(e.message ?: "An unexpected error occurred")
                Log.d("AuthStatus -> Login ->", "An unexpected error occurred")
                setAuthStatus(AuthStatus.ERROR)
            } finally {
                setLoading(false)
            }
        }
    }



    fun onSignupPressed() {
        navigationProvider.navigateTo(Routes.Register.route)
    }

    fun forgetPasswordRequest() {
        navigationProvider.navigateTo(Routes.ForgetPassword.route)
    }

    fun verifyEmail(){
        val email = _loginState.value.email
        viewModelScope.launch {
            try {
                val result = authRepository.sendPasswordResetEmail(email)
                if (result.isSuccess){
                    setEmailStatus(true)
                }
                else{
                    setErrorMessage(result.exceptionOrNull()?.message ?: "Email verification failed")
                    setEmailStatus(false)
                }
            }
            catch (e: Exception){
                setEmailStatus(false)
            }
        }
    }

    fun onLoginSuccess(){
        navigationProvider.goBack()
        navigationProvider.navigateTo(Routes.Home.route)
    }

    fun clearAuthStatus(){
        resetState()
    }
}

/**
 * Login form state
 */
data class LoginFormState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val isValidEmail: Boolean = true,
    val passwordError: String? = null,
    val isValidPassword: Boolean = true
)
