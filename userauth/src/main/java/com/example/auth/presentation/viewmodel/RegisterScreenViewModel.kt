package com.example.auth.presentation.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.auth.core.navigation.AuthNavigationProvider
import com.example.auth.core.navigation.Routes
import com.example.auth.data.repository.AuthRepositoryImpl
import com.example.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * View model for the registration screen
 */

class RegistrationScreenViewModel(
    private val context: Context,
    private val authRepository: AuthRepository,
    private val navigationProvider: AuthNavigationProvider
) : BaseAuthViewModel() {
    // Registration specific UI state

    private val _registrationState = MutableStateFlow(RegistrationFormState())
    val registrationState: StateFlow<RegistrationFormState> = _registrationState.asStateFlow()

    private val appContext = context.applicationContext

    private val authRepoImpl = authRepository as? AuthRepositoryImpl

    init {
        checkUserLoggedIn()
    }


    fun checkUserLoggedIn() {
        isLoggedIn.value = authRepository.isUserLoggedIn()
        Log.d("Firebase", "User Login Status ${isLoggedIn.value}")
    }

    fun signUpWithGoogle(): Intent {
        return authRepository.signUpWithGoogle()
    }


    // Update form values
    fun setUsername(value: String) {
        _registrationState.update {
            it.copy(username = value)
        }
        validateUsername()
    }

    fun setEmail(value: String) {
        _registrationState.update {
            it.copy(email = value)
        }
        Log.d("Email", _registrationState.value.email)
        validateEmail()
    }

    fun setPassword(value: String) {
        _registrationState.update {
            it.copy(password = value)
        }
        validatePassword()
        // Also validate confirm password as it depends on password
        if (_registrationState.value.confirmPassword.isNotEmpty()) {
            validateConfirmPassword()
        }
    }

    fun setConfirmPassword(value: String) {
        _registrationState.update {
            it.copy(confirmPassword = value)
        }
        validateConfirmPassword()
    }

    // Validation methods
    private fun validateUsername(): Boolean {
        val state = _registrationState.value
        val usernameValue = state.username

        return when {
            usernameValue.isEmpty() -> {
                _registrationState.update {
                    it.copy(usernameError = "Username cannot be empty", isValidUsername = false)
                }
                false
            }

            usernameValue.length < 3 -> {
                _registrationState.update {
                    it.copy(
                        usernameError = "Username must be at least 3 characters",
                        isValidUsername = false
                    )
                }
                false
            }

            else -> {
                _registrationState.update {
                    it.copy(usernameError = null, isValidUsername = true)
                }
                true
            }
        }
    }

    private fun validateEmail(): Boolean {
        val state = _registrationState.value
        val emailValue = state.email

        return when {
            emailValue.isEmpty() -> {
                _registrationState.update {
                    it.copy(emailError = "Email cannot be empty", isValidEmail = false)
                }
                false
            }

            !android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches() -> {
                _registrationState.update {
                    it.copy(emailError = "Please enter a valid email address", isValidEmail = false)
                }
                false
            }

            else -> {
                _registrationState.update {
                    it.copy(emailError = null, isValidEmail = true)
                }
                true
            }
        }
    }

    private fun validatePassword(): Boolean {
        val state = _registrationState.value
        val passwordValue = state.password

        return when {
            passwordValue.isEmpty() -> {
                _registrationState.update {
                    it.copy(passwordError = "Password cannot be empty", isValidPassword = false)
                }
                false
            }

            passwordValue.length < 8 -> {
                _registrationState.update {
                    it.copy(
                        passwordError = "Password must be at least 8 characters",
                        isValidPassword = false
                    )
                }
                false
            }

            !passwordValue.any { it.isDigit() } -> {
                _registrationState.update {
                    it.copy(
                        passwordError = "Password must contain at least one digit",
                        isValidPassword = false
                    )
                }
                false
            }

            !passwordValue.any { it.isUpperCase() } -> {
                _registrationState.update {
                    it.copy(
                        passwordError = "Password must contain at least one uppercase letter",
                        isValidPassword = false
                    )
                }
                false
            }

            else -> {
                _registrationState.update {
                    it.copy(passwordError = null, isValidPassword = true)
                }
                true
            }
        }
    }

    private fun validateConfirmPassword(): Boolean {
        val state = _registrationState.value
        val passwordValue = state.password
        val confirmPasswordValue = state.confirmPassword

        return when {
            confirmPasswordValue.isEmpty() -> {
                _registrationState.update {
                    it.copy(
                        confirmPasswordError = "Confirm password cannot be empty",
                        isValidConfirmPassword = false
                    )
                }
                false
            }

            confirmPasswordValue != passwordValue -> {
                _registrationState.update {
                    it.copy(
                        confirmPasswordError = "Passwords do not match",
                        isValidConfirmPassword = false
                    )
                }
                false
            }

            else -> {
                _registrationState.update {
                    it.copy(confirmPasswordError = null, isValidConfirmPassword = true)
                }
                true
            }
        }
    }

    // Validate all form fields
    private fun validateForm(): Boolean {
        val isUsernameValid = validateUsername()
        val isEmailValid = validateEmail()
        val isPasswordValid = validatePassword()
        val isConfirmPasswordValid = validateConfirmPassword()
        return isUsernameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid
    }

    // Register with email and password
    fun register() {
        if (!validateForm()) {
            setErrorMessage("Check all Input")
            setAuthStatus(AuthStatus.ERROR)
            return
        }
        // Set loading before launching coroutine
        setLoading(true)
        viewModelScope.launch {
            try {
                val state = _registrationState.value
                val result = authRepository.registerWithEmailPassword(
                    username = state.username,
                    email = state.email,
                    password = state.password
                )

                if (result.isSuccess) {
                    setAuthStatus(AuthStatus.SUCCESS)
                    Log.d("AuthStatus", "SUCCESS")
                } else {
                    setErrorMessage(result.exceptionOrNull()?.message ?: "Registration failed")
                    Log.d(
                        "AuthStatus",
                        "${result.exceptionOrNull()?.message} = Registration failed"
                    )
                    setAuthStatus(AuthStatus.ERROR)
                }
            } catch (e: Exception) {
                setErrorMessage(e.message ?: "An unexpected error occurred")
                Log.d("AuthStatus", "${e.message}: An unexpected error occurred")

                setAuthStatus(AuthStatus.ERROR)
            } finally {
                setLoading(false)
            }
        }
    }

    // Handle the result after Google sign-in activity
    fun handleGoogleSignInResult(data: Intent?) {
        setLoading(true)
        viewModelScope.launch {
            try {
                val result = authRepoImpl?.handleGoogleSignInResult(data)
                if (result != null) {
                    if (result.isSuccess) {
                        setAuthStatus(AuthStatus.SUCCESS)
                        Log.d("GoogleAuthStatus", "SUCCESS")
                    } else {
                        val errorMsg = result.exceptionOrNull()?.message ?: "Google sign-in failed"
                        setErrorMessage(errorMsg)
                        setAuthStatus(AuthStatus.ERROR)
                        Log.d("GoogleAuthStatus", "ERROR -> $errorMsg")
                    }
                }
            } catch (e: Exception) {
                setErrorMessage("Google sign-in failed: ${e.message}")
                setAuthStatus(AuthStatus.ERROR)
                Log.d("GoogleAuthStatus", "Google sign-in failed: ${e.message}")
            } finally {
                setLoading(false)
            }
        }
    }

    fun clearAuthStatus() {
        resetState()
    }


    fun onRegisterSuccess() {
        _registrationState.value = RegistrationFormState()
        navigationProvider.navigateTo(Routes.Login.route)
    }

    fun onLoginSuccess() {
        _registrationState.value = RegistrationFormState()
        navigationProvider.navigateTo(Routes.Home.route)
    }

    fun onLoginPressed() {
        _registrationState.value = RegistrationFormState()
        navigationProvider.navigateTo(Routes.Login.route)
    }
}


/**
 * Registration form state
 */
data class RegistrationFormState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val usernameError: String? = null,
    val isValidUsername: Boolean = true,
    val emailError: String? = null,
    val isValidEmail: Boolean = true,
    val passwordError: String? = null,
    val isValidPassword: Boolean = true,
    val confirmPasswordError: String? = null,
    val isValidConfirmPassword: Boolean = true,
)