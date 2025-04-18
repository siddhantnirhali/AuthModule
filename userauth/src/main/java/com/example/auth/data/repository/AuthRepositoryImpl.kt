package com.example.auth.data.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.auth.data.model.User
import com.example.auth.domain.repository.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(private val context: Context) : AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val googleAuthManager = GoogleAuthManager(context)

    override fun isUserLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    override fun currentUserData(): User {
        return User(
            userName = firebaseAuth.currentUser?.displayName,
            email = firebaseAuth.currentUser?.email
        )
    }

    // Get the sign-in intent for Google authentication
    fun getGoogleSignInIntent(): Intent {
        return googleAuthManager.getSignInIntent()
    }

    suspend fun signOut(): Result<Boolean> {
        return try {
            googleAuthManager.signOut()
            firebaseAuth.signOut()
            if (FirebaseAuth.getInstance().currentUser == null) {
                Log.d("FirebaseAuth", "User Successfully sign out googleAuthManager" )
                Result.success(true) // User signed out successfully

            } else {
                Log.d("FirebaseAuth", "Problem in sign out googleAuthManager" )
                Result.failure(Exception("User still logged in"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    // Process the Google Sign-In result
    suspend fun handleGoogleSignInResult(data: Intent?): Result<Unit> = try {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.getResult(ApiException::class.java)
        // Google Sign In was successful, authenticate with Firebase
        firebaseAuthWithGoogle(account)
    } catch (e: ApiException) {
        Log.w("GoogleSignIn", "Google sign in failed", e)
        Result.failure(Exception("Google sign in failed: ${e.message}"))
    } catch (e: Exception) {
        Log.e("GoogleSignIn", "Unexpected error", e)
        Result.failure(Exception("An unexpected error occurred: ${e.message}"))
    }

    // Authenticate with Firebase using Google credentials
    private suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount): Result<Unit> = try {
        val idToken = account.idToken!!
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = firebaseAuth.signInWithCredential(credential).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("GoogleSignIn", "Firebase authentication failed", e)
        Result.failure(Exception("Google authentication failed: ${e.message}"))
    }

    override suspend fun registerWithEmailPassword(
        username: String,
        email: String,
        password: String
    ): Result<Unit> = try {
        // Create the user account with Firebase Auth
        Log.d("DEBUG", "Before API Call")
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = firebaseAuth.currentUser
        Log.d("updateProfile", username)
        val profileUpdates = userProfileChangeRequest {
            displayName = username
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("updateProfile", "User profile updated.")
                }
            }
        Log.d("updateProfile", "${user.displayName}")
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

    override fun signUpWithGoogle(): Intent {
        // This method will be called from the ViewModel, but actual implementation
        // happens in handleGoogleSignInResult after the intent result
        return googleAuthManager.getSignInIntent()
    }

    override fun signInWithGoogle(): Intent {
        // This method will be called from the ViewModel, but actual implementation
        // happens in handleGoogleSignInResult after the intent result
        return googleAuthManager.getSignInIntent()
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