package com.evn.ev_ivi.features.auth.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evn.ev_ivi.features.auth.domain.usecases.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    var username by mutableStateOf("")
        private set
    
    var password by mutableStateOf("")
        private set
    
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    var isLoginSuccessful by mutableStateOf(false)
        private set

    fun updateUsername(newUsername: String) {
        username = newUsername
        errorMessage = null
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
        errorMessage = null
    }

    fun login() {
        if (username.isBlank() || password.isBlank()) {
            errorMessage = "Username and password cannot be empty"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                val result = loginUseCase(username, password)
                if (result.isSuccess) {
                    isLoginSuccessful = true
                } else {
                    errorMessage = result.exceptionOrNull()?.message ?: "Login failed"
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "An error occurred"
            } finally {
                isLoading = false
            }
        }
    }

    fun resetLoginState() {
        isLoginSuccessful = false
    }
}