package com.devid_academy.tutocomposeoct23

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devid_academy.tutocomposeoct23.ui.crea.CreaScreen
import com.devid_academy.tutocomposeoct23.ui.crea.CreaViewModel
import com.devid_academy.tutocomposeoct23.ui.edit.EditScreen
import com.devid_academy.tutocomposeoct23.ui.edit.EditViewModel
import com.devid_academy.tutocomposeoct23.ui.login.LoginScreen
import com.devid_academy.tutocomposeoct23.ui.login.LoginViewModel
import com.devid_academy.tutocomposeoct23.ui.main.MainScreen
import com.devid_academy.tutocomposeoct23.ui.main.MainViewModel
import com.devid_academy.tutocomposeoct23.ui.register.RegisterScreen
import com.devid_academy.tutocomposeoct23.ui.register.RegisterViewModel
import com.devid_academy.tutocomposeoct23.ui.splash.SplashViewModel
import com.devid_academy.tutocomposeoct23.ui.splash.SplashScreen

sealed class Screen(val route : String){

    object Splash   : Screen("splash")
    object Login    : Screen("login")
    object Register : Screen("register")
    object Main     : Screen("main")
    object Crea     : Screen("crea")
    object Edit     : Screen("edit")

}

@Composable
fun AppNavigation(){

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ){

        composable(Screen.Splash.route){
            val splashViewModel : SplashViewModel = hiltViewModel()
            SplashScreen(
                navController = navController,
                viewModel = splashViewModel
            )
        }

        composable(Screen.Login.route){
            val loginViewModel : LoginViewModel = hiltViewModel()
            LoginScreen(
                navController = navController,
                viewModel = loginViewModel
            )
        }

        composable(Screen.Register.route){
            val registerViewModel : RegisterViewModel = hiltViewModel()
            RegisterScreen(
                navController = navController,
                viewModel = registerViewModel
            )
        }

        composable(Screen.Main.route){
            val mainViewModel : MainViewModel = hiltViewModel()
            MainScreen(
                navController = navController,
                viewModel = mainViewModel)
        }

        composable(Screen.Crea.route){
            val creaViewModel : CreaViewModel = hiltViewModel()
            CreaScreen(
                navController = navController,
                viewModel = creaViewModel)
        }

        composable(Screen.Edit.route){
            val userViewModel : EditViewModel = hiltViewModel()
            EditScreen(
                navController = navController,
                viewModel = userViewModel)
        }

    }
}
