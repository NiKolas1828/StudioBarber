package com.dsmovil.studiobarber.ui.screens.navigation

sealed class Screen(val route: String) {
    // Auth
    data object AuthChooser : Screen("auth")
    data object Login : Screen("login")
    data object Register : Screen("register")

    // Client
    data object ClientHome : Screen("client_home")
    data object ClientReservations : Screen("client_reservations")

    // Admin
    data object AdminHome : Screen("admin_home")
    data object AdminBarbers : Screen("admin_barbers")
}