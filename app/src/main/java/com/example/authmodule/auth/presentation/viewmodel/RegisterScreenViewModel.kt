package com.example.authmodule.auth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
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
 * View model for the registration screen
 */

class RegistrationScreenViewModel (private val authRepository: AuthRepository, private val navigationProvider: AuthNavigationProvider
) : BaseAuthViewModel() {
    // Registration specific UI state

    private val _registrationState = MutableStateFlow(RegistrationFormState())
    val registrationState: StateFlow<RegistrationFormState> = _registrationState.asStateFlow()

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
                    it.copy(usernameError = "Username cannot be empty")
                }
                false
            }

            usernameValue.length < 3 -> {
                _registrationState.update {
                    it.copy(usernameError = "Username must be at least 3 characters")
                }
                false
            }

            else -> {
                _registrationState.update {
                    it.copy(usernameError = null)
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
                    it.copy(emailError = "Email cannot be empty")
                }
                false
            }

            !android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches() -> {
                _registrationState.update {
                    it.copy(emailError = "Please enter a valid email address")
                }
                false
            }

            else -> {
                _registrationState.update {
                    it.copy(emailError = null)
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
                    it.copy(passwordError = "Password cannot be empty")
                }
                false
            }

            passwordValue.length < 8 -> {
                _registrationState.update {
                    it.copy(passwordError = "Password must be at least 8 characters")
                }
                false
            }

            !passwordValue.any { it.isDigit() } -> {
                _registrationState.update {
                    it.copy(passwordError = "Password must contain at least one digit")
                }
                false
            }

            !passwordValue.any { it.isUpperCase() } -> {
                _registrationState.update {
                    it.copy(passwordError = "Password must contain at least one uppercase letter")
                }
                false
            }

            else -> {
                _registrationState.update {
                    it.copy(passwordError = null)
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
                    it.copy(confirmPasswordError = "Confirm password cannot be empty")
                }
                false
            }

            confirmPasswordValue != passwordValue -> {
                _registrationState.update {
                    it.copy(confirmPasswordError = "Passwords do not match")
                }
                false
            }

            else -> {
                _registrationState.update {
                    it.copy(confirmPasswordError = null)
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
            return
        }
        Log.d("ComposeCheck", "Recomposition register pressed")
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
                    //delay(2000)
                    setLoading(false)

                    Log.d(
                        "AuthStatus",
                        "Setting loading to false, current value: ${uiState.value.isLoading}"
                    )
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
                // Update loading state directly at end of operation


            }
        }
    }

    // Google sign up
    override fun authenticateWithGoogle() {
        setLoading(true)
        viewModelScope.launch {
            try {
                val result = authRepository.signUpWithGoogle()

                if (result.isSuccess) {
                    setAuthStatus(AuthStatus.SUCCESS)
                } else {
                    setErrorMessage(result.exceptionOrNull()?.message ?: "Google sign up failed")
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

    // Facebook sign up
    override fun authenticateWithFacebook() {
        setLoading(true)
        viewModelScope.launch {
            try {
                val result = authRepository.signUpWithFacebook()

                if (result.isSuccess) {
                    setAuthStatus(AuthStatus.SUCCESS)
                } else {
                    setErrorMessage(result.exceptionOrNull()?.message ?: "Facebook sign up failed")
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

    fun onLoginPressed() {
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
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)