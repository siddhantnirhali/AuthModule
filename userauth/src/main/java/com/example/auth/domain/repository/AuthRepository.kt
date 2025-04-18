package com.example.auth.domain.repository

import android.content.Intent
import com.example.auth.data.model.User

/**
 * Repository interface for authentication operations
 * This would be implemented according to your specific authentication backend
 */

interface AuthRepository {
    suspend fun registerWithEmailPassword(username: String, email: String, password: String): Result<Unit>
    suspend fun loginWithEmailPassword(email: String, password: String): Result<Unit>
    fun signUpWithGoogle(): Intent
    fun signInWithGoogle(): Intent
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    fun currentUserData(): User
    fun isUserLoggedIn(): Boolean
}