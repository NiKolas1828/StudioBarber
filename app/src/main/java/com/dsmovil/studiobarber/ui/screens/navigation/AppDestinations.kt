package com.dsmovil.studiobarber.ui.screens.navigation

sealed class Screen(val route: String) {
    // Auth
    data object AuthChooser : Screen("auth")
    data object Login : Screen("login")
    data object Register : Screen("register")

    // Client
    data object ClientReservations : Screen("client_reservations")

    data object ClientHome : Screen("client_home?reservationId={reservationId}&serviceId={serviceId}&barberId={barberId}") {
        fun createRoute(
            reservationId: Long? = null,
            serviceId: Long? = null,
            barberId: Long? = null
        ): String {
            return "client_home?reservationId=${reservationId ?: ""}&serviceId=${serviceId ?: -1}&barberId=${barberId ?: -1}"
        }
    }

    data object ClientCalendar : Screen("client_calendar/{serviceId}/{barberId}?reservationId={reservationId}&prevDate={prevDate}&prevTime={prevTime}") {
        fun createRoute(
            serviceId: Long,
            barberId: Long,
            reservationId: Long?,
            prevDate: String? = null,
            prevTime: String? = null
        ): String {
            val rId = reservationId ?: -1L
            return "client_calendar/$serviceId/$barberId?reservationId=$rId&prevDate=${prevDate ?: ""}&prevTime=${prevTime ?: ""}"
        }
    }

    // Admin
    data object AdminHome : Screen("admin_home")
    data object AdminBarbers : Screen("admin_barbers")
    data object AdminServices : Screen("admin_services")
    data object AdminReservations : Screen("admin_reservations")

    //barber
    data object BarberSchedule : Screen("barber_schedule")

}