package com.example.authmodule.auth.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.authmodule.auth.core.navigation.AuthNavigationProvider
import com.example.authmodule.auth.core.navigation.Routes
import com.example.authmodule.auth.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * View model for the login screen
 */

class LoginViewModel(
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
                    it.copy(emailError = "Email cannot be empty")
                }
                false
            }

            !android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches() -> {
                _loginState.update {
                    it.copy(emailError = "Please enter a valid email address")
                }
                false
            }

            else -> {
                _loginState.update {
                    it.copy(emailError = null)
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
                    it.copy(passwordError = "Password cannot be empty")
                }
                false
            }

            else -> {
                _loginState.update {
                    it.copy(passwordError = null)
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
                Log.d(
                    "AuthStatus -> Login User ->", "email = ${state.email},\n" +
                            "                    password = ${state.password}"
                )
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
                viewModelScope.launch {
                    //delay(2000)
                    setLoading(false)
                }
            }
        }
    }

    // Google sign in
    override fun authenticateWithGoogle() {
        setLoading(true)
        viewModelScope.launch {
            try {
                val result = authRepository.signInWithGoogle()

                if (result.isSuccess) {
                    setAuthStatus(AuthStatus.SUCCESS)
                } else {
                    setErrorMessage(result.exceptionOrNull()?.message ?: "Google sign in failed")
                    setAuthStatus(AuthStatus.ERROR)
                }
            } catch (e: Exception) {
                setErrorMessage(e.message ?: "An unexpected error occurred")
                setAuthStatus(AuthStatus.ERROR)
            } finally {
                setLoading(false)
            }
        }
    }

    // Facebook sign in
    override fun authenticateWithFacebook() {
        setLoading(true)
        viewModelScope.launch {
            try {
                val result = authRepository.signInWithFacebook()

                if (result.isSuccess) {
                    setAuthStatus(AuthStatus.SUCCESS)
                } else {
                    setErrorMessage(result.exceptionOrNull()?.message ?: "Facebook sign in failed")
                    setAuthStatus(AuthStatus.ERROR)
                }
            } catch (e: Exception) {
                setErrorMessage(e.message ?: "An unexpected error occurred")
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
}

/**
 * Login form state
 */
data class LoginFormState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null
)
