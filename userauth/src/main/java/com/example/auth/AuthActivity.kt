package com.example.auth

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.example.auth.core.di.AppContainer
import com.example.auth.presentation.AuthApp

class AuthActivity : ComponentActivity() {

    private val appContainer = AppContainer(this)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuthApp(appContainer, this)
        }
    }
}

