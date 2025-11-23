package com.dsmovil.studiobarber.ui.screens.navigation

sealed class Screen(val route: String) {
    // Auth
    data object AuthChooser : Screen("auth")
    data object Login : Screen("login")
    data object Register : Screen("register")

    // Client
    data object Home : Screen("home")

    // Admin
    data object AdminHome : Screen("admin_home")
    data object AdminBarbers : Screen("admin_barbers")
}