package com.dsmovil.studiobarber.ui.screens.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dsmovil.studiobarber.domain.models.Role
import com.dsmovil.studiobarber.ui.screens.admin.barbers.ManageBarbersScreen
import com.dsmovil.studiobarber.ui.screens.admin.barbers.ManageBarbersViewModel
import com.dsmovil.studiobarber.ui.screens.admin.home.AdminDashboardScreen
import com.dsmovil.studiobarber.ui.screens.admin.home.DashboardViewModel
import com.dsmovil.studiobarber.ui.screens.admin.reservations.AdminReservationsScreen
import com.dsmovil.studiobarber.ui.screens.admin.reservations.AdminReservationsViewModel
import com.dsmovil.studiobarber.ui.screens.admin.services.ManageServicesScreen
import com.dsmovil.studiobarber.ui.screens.admin.services.ManageServicesViewModel
import com.dsmovil.studiobarber.ui.screens.auth.AuthChooserScreen
import com.dsmovil.studiobarber.ui.screens.client.calendar.ClientCalendarScreen
import com.dsmovil.studiobarber.ui.screens.client.home.ClientHomeScreen
import com.dsmovil.studiobarber.ui.screens.client.home.ClientHomeViewModel
import com.dsmovil.studiobarber.ui.screens.client.calendar.ClientCalendarViewModel
import com.dsmovil.studiobarber.ui.screens.client.reservations.ClientReservationViewModel
import com.dsmovil.studiobarber.ui.screens.client.reservations.ClientReservationsScreen
import com.dsmovil.studiobarber.ui.screens.login.LoginScreen
import com.dsmovil.studiobarber.ui.screens.login.LoginViewModel
import com.dsmovil.studiobarber.ui.screens.register.RegisterScreen
import com.dsmovil.studiobarber.ui.screens.register.RegisterViewModel

fun NavGraphBuilder.authGraph(navController: NavController) {
    composable(Screen.AuthChooser.route) {
        AuthChooserScreen(
            goToLogin = { navController.navigate(Screen.Login.route) },
            goToRegister = { navController.navigate(Screen.Register.route) }
        )
    }

    composable(Screen.Login.route) {
        val viewModel: LoginViewModel = hiltViewModel()

        LoginScreen(
            viewModel = viewModel,
            onLoginSuccess = { role ->
                val destination = when (role) {
                    Role.CLIENTE -> Screen.ClientHome.route
                    Role.ADMINISTRADOR -> Screen.AdminHome.route
                    Role.BARBERO -> Screen.ClientHome.route
                }

                navController.navigate(destination) {
                    popUpTo(Screen.AuthChooser.route) { inclusive = true }
                }
            }
        )
    }

    composable(Screen.Register.route) {
        val viewModel: RegisterViewModel = hiltViewModel()

        RegisterScreen(
            viewModel = viewModel,
            onRegisterSuccess = {
                navController.navigate(Screen.ClientHome.route) {
                    popUpTo(Screen.AuthChooser.route) { inclusive = true }
                }
            }
        )
    }
}

fun NavGraphBuilder.clientGraph(navController: NavController) {
    composable(Screen.ClientHome.route) {
        val viewModel: ClientHomeViewModel = hiltViewModel()

        ClientHomeScreen(
            onNavigateToClientReservarionts = { navController.navigate(Screen.ClientReservations.route) },
            onLogout = { navigateToAuthAndClearStack(navController) },
            onContinueClick = { serviceId, barberId ->
                navController.navigate(Screen.ClientCalendar.createRoute(serviceId, barberId))
            },
            viewModel = viewModel
        )
    }

    composable(Screen.ClientReservations.route) {
        val viewModel: ClientReservationViewModel = hiltViewModel()

        ClientReservationsScreen(
            onNavigateToClientHome = { navController.navigate(Screen.ClientHome.route) },
            viewModel = viewModel,
            onLogout = { navigateToAuthAndClearStack(navController)}
        )
    }
    composable(
        route = Screen.ClientCalendar.route,
        arguments = listOf(
            navArgument("serviceId") { type = NavType.LongType },
            navArgument("barberId") { type = NavType.LongType }
        )
    ) {
        val viewModel: ClientCalendarViewModel = hiltViewModel()

        ClientCalendarScreen(
            viewModel = viewModel,
            onNavigateBack = { navController.navigate(Screen.ClientHome.route) },
            onMyReservationsClick = { navController.navigate(Screen.ClientReservations.route) },
            onLogout = { navigateToAuthAndClearStack(navController) }
        )
    }
}

fun NavGraphBuilder.adminGraph(navController: NavController) {
    composable(Screen.AdminHome.route) {
        val viewModel: DashboardViewModel = hiltViewModel()

        AdminDashboardScreen(
            viewModel = viewModel,
            onNavigateToServices = { navController.navigate(Screen.AdminServices.route) },
            onNavigateToBarbers = { navController.navigate(Screen.AdminBarbers.route) },
            onNavigateToReservations = { navController.navigate(Screen.AdminReservations.route) },
            onLogout = { navigateToAuthAndClearStack(navController) }
        )
    }

    composable(Screen.AdminBarbers.route) {
        val viewModel: ManageBarbersViewModel = hiltViewModel()

        ManageBarbersScreen(
            viewModel = viewModel,
            onNavigateBack = { navController.popBackStack() },
            onLogout = { navigateToAuthAndClearStack(navController) }
        )
    }

    composable(Screen.AdminServices.route) {
        val viewModel: ManageServicesViewModel = hiltViewModel()

        ManageServicesScreen(
            viewModel = viewModel,
            onNavigateBack = { navController.popBackStack() },
            onLogout = { navigateToAuthAndClearStack(navController) }
        )
    }

    composable(Screen.AdminReservations.route) {
        val viewModel: AdminReservationsViewModel = hiltViewModel()

        AdminReservationsScreen(
            viewModel = viewModel,
            onNavigateToDashboard = { navController.navigate(Screen.AdminHome.route) },
            onLogout = { navigateToAuthAndClearStack(navController) }
        )
    }
}

private fun navigateToAuthAndClearStack(navController: NavController) {
    navController.navigate(Screen.AuthChooser.route) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}