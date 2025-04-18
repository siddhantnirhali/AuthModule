package com.example.auth.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.auth.presentation.responsive.WindowType
import com.example.auth.presentation.screens.ForgetPasswordScreen
import com.example.auth.presentation.screens.HomeScreen
import com.example.auth.presentation.screens.LoginScreen
import com.example.auth.presentation.screens.RegisterScreen
import com.example.auth.presentation.viewmodel.HomeScreenViewModel
import com.example.auth.presentation.viewmodel.LoginViewModel
import com.example.auth.presentation.viewmodel.RegistrationScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(windowType: WindowType,
             navController: NavHostController,
             loginViewModel: LoginViewModel,
             registrationScreenViewModel: RegistrationScreenViewModel,
             homeScreenViewModel: HomeScreenViewModel
) {

    val isLoggedIn by registrationScreenViewModel.isLoggedIn.collectAsState()
    val startDestination = when (isLoggedIn) {
        true -> Routes.Home.route
        false -> Routes.Register.route
    }

    NavHost(navController = navController, startDestination = startDestination) {
        addLoginScreen(windowType, loginViewModel)
        addRegisterScreen(windowType, registrationScreenViewModel)
        addForgetPasswordScreen(windowType, loginViewModel)
        addHomeScreen(windowType, homeScreenViewModel)
    }
}

private fun NavGraphBuilder.addLoginScreen(windowType: WindowType, viewModel: LoginViewModel) {
    composable(Routes.Login.route) {
        LoginScreen(windowType, viewModel)
    }
}

private fun NavGraphBuilder.addRegisterScreen(windowType: WindowType, viewModel: RegistrationScreenViewModel) {
    composable(Routes.Register.route) {
        RegisterScreen(windowType, viewModel)
    }
}

private fun NavGraphBuilder.addForgetPasswordScreen(windowType: WindowType, viewModel: LoginViewModel) {
    composable(Routes.ForgetPassword.route) {
        ForgetPasswordScreen(windowType, viewModel)
    }
}

private fun NavGraphBuilder.addHomeScreen(windowType: WindowType, viewModel: HomeScreenViewModel) {
    composable(Routes.Home.route) {
        HomeScreen(windowType, viewModel)
    }
}