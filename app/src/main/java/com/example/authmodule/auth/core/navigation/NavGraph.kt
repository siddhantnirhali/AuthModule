package com.example.authmodule.auth.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.authmodule.auth.presentation.screens.ForgetPasswordScreen
import com.example.authmodule.auth.presentation.screens.LoginScreen
import com.example.authmodule.auth.presentation.screens.RegisterScreen
import com.example.authmodule.auth.presentation.viewmodel.LoginViewModel
import com.example.authmodule.auth.presentation.viewmodel.RegistrationScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController, loginViewModel: LoginViewModel, registrationScreenViewModel: RegistrationScreenViewModel) {
    NavHost(navController = navController, startDestination = Routes.Register.route) {
        addLoginScreen(loginViewModel)
        addRegisterScreen(registrationScreenViewModel)
        addForgetPasswordScreen(loginViewModel)
    }
}

private fun NavGraphBuilder.addLoginScreen(viewModel: LoginViewModel) {
    composable(Routes.Login.route) {
        LoginScreen(viewModel)
    }
}

private fun NavGraphBuilder.addRegisterScreen(viewModel: RegistrationScreenViewModel) {
    composable(Routes.Register.route) {
        RegisterScreen(viewModel)
    }
}
private fun NavGraphBuilder.addForgetPasswordScreen(viewModel: LoginViewModel) {
    composable(Routes.ForgetPassword.route) {
        ForgetPasswordScreen(viewModel)
    }
}