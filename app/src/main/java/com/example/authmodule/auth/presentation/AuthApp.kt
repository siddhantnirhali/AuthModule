package com.example.authmodule.auth.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.authmodule.auth.core.di.AppContainer
import com.example.authmodule.auth.core.navigation.NavGraph

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthApp(appContainer: AppContainer) {
    val navController = rememberNavController()
    appContainer.navigationProvider.setNavController(navController)

    NavGraph(
        navController = navController,
        loginViewModel = appContainer.provideLoginViewModel(),
        registrationScreenViewModel = appContainer.provideRegisterViewModel()
    )
}
