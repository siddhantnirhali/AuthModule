package com.example.auth.core.di

import android.content.Context
import com.example.auth.core.navigation.AuthNavigationProvider
import com.example.auth.data.repository.AuthRepositoryImpl
import com.example.auth.domain.repository.AuthRepository
import com.example.auth.presentation.viewmodel.HomeScreenViewModel
import com.example.auth.presentation.viewmodel.LoginViewModel
import com.example.auth.presentation.viewmodel.RegistrationScreenViewModel

class AppContainer(context: Context) {

    private val authRepository: AuthRepository = AuthRepositoryImpl(context)
    val navigationProvider = AuthNavigationProvider()


    // Hold the ViewModels in memory
    private val loginViewModel by lazy {
        LoginViewModel(context, authRepository, navigationProvider)
    }

    private val registerViewModel by lazy {
        RegistrationScreenViewModel(context, authRepository, navigationProvider)
    }

    private val homeViewModel by lazy {
        HomeScreenViewModel(context, authRepository, navigationProvider)
    }

    fun provideLoginViewModel() = loginViewModel

    fun provideRegisterViewModel() = registerViewModel

    fun provideHomeViewModel() = homeViewModel
}
