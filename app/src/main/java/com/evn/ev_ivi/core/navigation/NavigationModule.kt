package com.evn.ev_ivi.core.navigation

import androidx.navigation.NavController
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val navigationModule = module {
    factory { (navController: NavController) -> navController }
    viewModel { NavigationViewModel(get()) }
}