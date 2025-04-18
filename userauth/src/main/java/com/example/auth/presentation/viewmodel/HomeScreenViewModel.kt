package com.example.auth.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.auth.core.navigation.AuthNavigationProvider
import com.example.auth.core.navigation.Routes
import com.example.auth.data.model.User
import com.example.auth.data.repository.AuthRepositoryImpl
import com.example.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val context: Context,
                          private val authRepository: AuthRepository,
                          private val navigationProvider: AuthNavigationProvider
): BaseAuthViewModel() {

    private val authRepoImpl = authRepository as? AuthRepositoryImpl
    private val _user = MutableStateFlow(
        User(
        userName = "",
        email = ""
    )
    )
    val user : StateFlow<User> = _user

    fun onScreenEntered() {
        checkUserLoggedIn()
    }
    private fun checkUserLoggedIn() {
        isLoggedIn.value = authRepository.isUserLoggedIn()
        if(isLoggedIn.value){
            getCurrentUserDetails()
        }
    }

    private fun getCurrentUserDetails(){
        val currentUser = authRepoImpl?.currentUserData()

        if (currentUser != null) {
            _user.value = _user.value.copy(userName = currentUser.userName, email = currentUser.email)
        }
    }
    fun signOut(){
        setLoading(true)
        viewModelScope.launch {
            val result = authRepoImpl?.signOut()
            result?.onSuccess {
                viewModelScope.launch {
                    isLoggedIn.value = false
                }
                setLoading(false)
                onSignOutSuccess()
                Log.d("FirebaseAuth", "User Successfully sign out")
            }?.onFailure { error ->
                isLoggedIn.value = true
                Log.d("FirebaseAuth", error.message.toString() )
            }
        }
    }

    private fun onSignOutSuccess(){
        navigationProvider.goBack()
        navigationProvider.navigateTo(Routes.Register.route)
        _user.value = User(
            userName = "",
            email = ""
        )
    }
}