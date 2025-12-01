package com.example.ferrazconectaapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ferrazconectaapp.ui.screens.CadastroScreen
import com.example.ferrazconectaapp.ui.screens.CandidaturasScreen
import com.example.ferrazconectaapp.ui.screens.ForgotPasswordScreen
import com.example.ferrazconectaapp.ui.screens.HomeScreen
import com.example.ferrazconectaapp.ui.screens.LoginScreen
import com.example.ferrazconectaapp.ui.screens.ProfileScreen
import com.example.ferrazconectaapp.ui.screens.VagaDetailsScreen

object Routes {
    const val LOGIN = "login"
    const val CADASTRO = "cadastro"
    const val HOME = "home"
    const val PERFIL = "perfil"
    const val CANDIDATURAS = "candidaturas"
    const val FORGOT_PASSWORD = "forgot_password"
    const val VAGA_DETAILS = "vaga_details/{vagaId}"

    fun createVagaDetailsRoute(vagaId: Int) = "vaga_details/$vagaId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToCadastro = { navController.navigate(Routes.CADASTRO) },
                onLoginSuccess = { 
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) }
            )
        }
        composable(Routes.CADASTRO) {
            CadastroScreen(
                onNavigateBack = { navController.popBackStack() },
                onLoginSuccess = { navController.popBackStack() }
            )
        }
        composable(Routes.HOME) {
            HomeScreen(navController = navController)
        }
        composable(Routes.PERFIL) {
            ProfileScreen(navController = navController)
        }
        composable(Routes.CANDIDATURAS) {
            CandidaturasScreen(navController = navController)
        }
        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(navController = navController)
        }
        composable(
            route = Routes.VAGA_DETAILS,
            arguments = listOf(navArgument("vagaId") { type = NavType.IntType })
        ) {
            VagaDetailsScreen(navController = navController)
        }
    }
}
