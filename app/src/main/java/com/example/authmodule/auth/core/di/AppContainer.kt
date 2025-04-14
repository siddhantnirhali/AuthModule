package com.example.authmodule.auth.core.di

import com.example.authmodule.auth.core.navigation.AuthNavigationProvider
import com.example.authmodule.auth.data.repository.AuthRepositoryImpl
import com.example.authmodule.auth.domain.repository.AuthRepository
import com.example.authmodule.auth.presentation.viewmodel.LoginViewModel
import com.example.authmodule.auth.presentation.viewmodel.RegistrationScreenViewModel

class AppContainer {

    private val authRepository: AuthRepository = AuthRepositoryImpl()
    val navigationProvider = AuthNavigationProvider()

    fun provideLoginViewModel() = LoginViewModel(authRepository, navigationProvider)

    fun provideRegisterViewModel() = RegistrationScreenViewModel(authRepository, navigationProvider)
}
