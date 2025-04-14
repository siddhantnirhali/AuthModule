package com.example.authmodule.auth.domain.repository

/**
 * Repository interface for authentication operations
 * This would be implemented according to your specific authentication backend
 */

interface AuthRepository {
    suspend fun registerWithEmailPassword(username: String, email: String, password: String): Result<Unit>
    suspend fun loginWithEmailPassword(email: String, password: String): Result<Unit>
    suspend fun signUpWithGoogle(): Result<Unit>
    suspend fun signInWithGoogle(): Result<Unit>
    suspend fun signUpWithFacebook(): Result<Unit>
    suspend fun signInWithFacebook(): Result<Unit>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
}