package com.example.authmodule.auth.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.example.authmodule.auth.domain.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl : AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val user = Firebase.auth.currentUser
    override suspend fun registerWithEmailPassword(
        username: String,
        email: String,
        password: String
    ): Result<Unit> = try {
        // Create the user account with Firebase Auth
        Log.d("DEBUG", "Before API Call")
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = authResult.user ?: throw IllegalStateException("User is null after registration")



        Log.d("DEBUG", "After API Call: Success")
        Result.success(Unit)

    } catch (e: Exception) {
        // Handle specific error cases with more detailed messages
        val errorMessage = when {
            e.message?.contains("email address is already in use") == true ->
                "This email is already registered. Please use another email or try logging in."

            e.message?.contains("password is invalid") == true ->
                "Password should be at least 6 characters long."

            e.message?.contains("network error") == true ->
                "Network error. Please check your internet connection and try again."

            else -> e.message ?: "Registration failed. Please try again."
        }

        Result.failure(Exception(errorMessage))

    }

    override suspend fun loginWithEmailPassword(email: String, password: String): Result<Unit> =
        try {
            // Create the user account with Firebase Auth
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            Result.success(Unit)
        } catch (e: Exception) {
            // Handle specific error cases with more detailed messages
            val errorMessage = when {
                e.message?.contains("password is invalid") == true ->
                    "password is invalid."

                e.message?.contains("network error") == true ->
                    "Network error. Please check your internet connection and try again."

                else -> e.message ?: "Registration failed. Please try again."
            }
            Result.failure(Exception(errorMessage))
        }

    override suspend fun signUpWithGoogle(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun signInWithGoogle(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun signUpWithFacebook(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun signInWithFacebook(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> = try {

        val result = firebaseAuth.sendPasswordResetEmail(email).await()
        Log.d("DEBUG", "Email Sent")
        Result.success(Unit)
    } catch (e: Exception) {
        Log.d("DEBUG", "Something is wrong")

        Result.failure(Exception(e))
    }
}