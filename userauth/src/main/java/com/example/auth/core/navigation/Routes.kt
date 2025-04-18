package com.example.auth.core.navigation


sealed class Routes(val route: String) {
    data object Login : Routes("login")
    data object Register : Routes("register")
    data object ForgetPassword : Routes("forgetPassword")
    data object Home : Routes("home")
}

