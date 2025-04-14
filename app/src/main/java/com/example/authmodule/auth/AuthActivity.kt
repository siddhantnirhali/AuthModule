package com.example.authmodule.auth

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.example.authmodule.auth.core.di.AppContainer
import com.example.authmodule.auth.data.repository.AuthRepositoryImpl
import com.example.authmodule.auth.core.navigation.AuthNavigationProvider
import com.example.authmodule.auth.core.navigation.NavGraph
import com.example.authmodule.auth.presentation.AuthApp
import com.example.authmodule.auth.presentation.viewmodel.LoginViewModel
import com.example.authmodule.auth.presentation.viewmodel.RegistrationScreenViewModel

class AuthActivity : ComponentActivity() {

    private val appContainer = AppContainer()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuthApp(appContainer)
        }
    }
}

