package com.example.ferrazconectaapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ferrazconectaapp.ui.screens.CadastroScreen
import com.example.ferrazconectaapp.ui.screens.HomeScreen
import com.example.ferrazconectaapp.ui.screens.LoginScreen

object Routes {
    const val LOGIN = "login"
    const val CADASTRO = "cadastro"
    const val HOME = "home"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToCadastro = { navController.navigate(Routes.CADASTRO) },
                onLoginSuccess = { navController.navigate(Routes.HOME) }
            )
        }
        composable(Routes.CADASTRO) {
            CadastroScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.HOME) {
            HomeScreen()
        }
    }
}
