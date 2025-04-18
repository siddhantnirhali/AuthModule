package com.example.auth.presentation

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.auth.core.di.AppContainer
import com.example.auth.presentation.responsive.getWindowType
import com.example.auth.core.navigation.NavGraph

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthApp(appContainer: AppContainer, context: Context) {
    val navController = rememberNavController()

    val windowSizeClass = calculateWindowSizeClass(context as Activity)
    val windowType =
        getWindowType(windowSizeClass.widthSizeClass)

    // Set the NavController to the NavigationProvider
    appContainer.navigationProvider.setNavController(navController)

    NavGraph(windowType,
        navController = navController,
        loginViewModel = appContainer.provideLoginViewModel(),
        registrationScreenViewModel = appContainer.provideRegisterViewModel(),
        homeScreenViewModel = appContainer.provideHomeViewModel()
    )
}
