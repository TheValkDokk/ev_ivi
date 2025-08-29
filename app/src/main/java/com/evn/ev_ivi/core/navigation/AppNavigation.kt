package com.evn.ev_ivi.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.evn.ev_ivi.features.auth.presentation.screen.LoginScreen
import com.evn.ev_ivi.features.map.presentation.screen.MapPanelScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navigationViewModel: NavigationViewModel = koinViewModel()
    
    NavHost(
        navController = navController,
        startDestination = navigationViewModel.getStartDestination()
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }
        composable(Routes.MAP) {
            MapPanelScreen()
        }
    }
}

object Routes {
    const val LOGIN = "login"
    const val MAP = "map"
}