package com.example.auth.data.repository
import android.content.Context
import android.content.Intent
import com.example.auth.BuildConfig
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.tasks.await



class GoogleAuthManager(private val context: Context) {
    private val webClientId = BuildConfig.WEB_CLIENT_ID // Replace with your actual client ID from Firebase console

    // Configure Google Sign-In
    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

        GoogleSignIn.getClient(context, gso)
    }

    // Get sign-in intent
    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    // Sign out
    suspend fun signOut() {
        googleSignInClient.signOut().await()

    }
}