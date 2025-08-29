package com.evn.ev_ivi.core.navigation

import androidx.lifecycle.ViewModel
import com.evn.ev_ivi.core.common.domain.usecase.IsLoggedInUseCase

class NavigationViewModel(
    private val isLoggedInUseCase: IsLoggedInUseCase
) : ViewModel() {
    
    fun getStartDestination(): String {
        return if (isLoggedInUseCase()) {
            Routes.MAP
        } else {
            Routes.LOGIN
        }
    }
}