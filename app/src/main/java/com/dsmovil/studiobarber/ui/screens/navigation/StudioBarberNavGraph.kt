package com.dsmovil.studiobarber.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun StudioBarberNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.ClientHome.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(navController)
        clientGraph(navController)
        adminGraph(navController)
    }
}