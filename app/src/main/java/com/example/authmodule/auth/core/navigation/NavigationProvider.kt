package com.example.authmodule.auth.core.navigation



import androidx.navigation.NavHostController

class AuthNavigationProvider {
    private lateinit var navController: NavHostController

    fun setNavController(controller: NavHostController) {
        this.navController = controller
    }

    fun navigateTo(route: String) {
        navController.navigate(route)
    }

    fun goBack() {
        navController.popBackStack()
    }
}