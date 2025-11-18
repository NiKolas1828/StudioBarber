package com.dsmovil.studiobarber

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dsmovil.studiobarber.ui.theme.StudioBarberTheme
import com.dsmovil.studiobarber.ui.screens.login.LoginScreen
import com.dsmovil.studiobarber.ui.screens.login.LoginViewModel
import com.dsmovil.studiobarber.domain.usecases.LoginUseCase
import com.dsmovil.studiobarber.data.repositories.AuthRepositoryImpl
import com.dsmovil.studiobarber.ui.screens.auth.AuthChooserScreen
import com.dsmovil.studiobarber.ui.screens.register.RegisterScreen
import com.dsmovil.studiobarber.ui.screens.home.HomeScreen
import com.dsmovil.studiobarber.ui.screens.register.RegisterViewModel
import com.dsmovil.studiobarber.domain.usecases.RegisterUseCase
import com.dsmovil.studiobarber.ui.screens.login.LoginViewModelFactory
import com.dsmovil.studiobarber.ui.screens.register.RegisterViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            StudioBarberTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "auth"
                ) {

                    // ----------------------------
                    // Pantalla inicial Auth
                    // ----------------------------
                    composable("auth") {
                        AuthChooserScreen(
                            goToLogin = { navController.navigate("login") },
                            goToRegister = { navController.navigate("register") }
                        )
                    }

                    // ----------------------------
                    // Pantalla de LOGIN real
                    // ----------------------------
                    composable("login") {
                        val loginViewModel: LoginViewModel = viewModel(
                            factory = LoginViewModelFactory()
                        )

                        LoginScreen(
                            viewModel = loginViewModel,
                            onLoginSuccess = {
                                navController.navigate("home") {
                                    popUpTo("auth") { inclusive = true }
                                }
                            }
                        )
                    }

                    // ----------------------------
                    // Pantalla de REGISTRO
                    // ----------------------------
                    composable("register") {
                        val registerViewModel: RegisterViewModel = viewModel(
                            factory = RegisterViewModelFactory()
                        )

                        RegisterScreen(
                            viewModel = registerViewModel,
                            onRegisterSuccess = {
                                navController.navigate("home") {
                                    popUpTo("auth") { inclusive = true }
                                }
                            }
                        )
                    }

                    // ----------------------------
                    // Pantalla HOME
                    // ----------------------------
                    composable("home") {
                        HomeScreen()
                    }
                }
            }
        }
    }
}