package com.dsmovil.studiobarber.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsmovil.studiobarber.R
import com.dsmovil.studiobarber.domain.models.Reservation
import androidx.compose.material3.Text
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.dsmovil.studiobarber.domain.models.Role

@Composable
fun ReservationCard(
    reservation: Reservation,
    modifier: Modifier = Modifier,
    onEditClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null,
    userRole: Role
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ReservationContent(reservation = reservation, userRole = userRole)
            Spacer(modifier = Modifier.width(38.dp))

            if (onEditClick != null || onDeleteClick != null) {
                ActionButtons(
                    firstIcon = ImageVector.vectorResource(id = R.drawable.ic_edit),
                    onFirstClick = onEditClick,
                    firstIconTint = colorResource(id = R.color.icon_color_red),
                    firstContentDescription = "Editar",

                    secondIcon = ImageVector.vectorResource(id = R.drawable.ic_trash),
                    onSecondClick = onDeleteClick,
                    secondIconTint = colorResource(id = R.color.icon_color_red),
                    secondContentDescription = "Eliminar"
                )
            }
        }
    }
}

@Composable
private fun ReservationContent(reservation: Reservation, userRole: Role) {
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        val labelStyle = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)
        val valueStyle = SpanStyle(color = Color.DarkGray)
        val fontSize = 15.sp

        val items = buildList {
            add("Fecha" to reservation.date)
            add("Hora" to reservation.timeStart)
            add("Servicio" to reservation.nameService)

            when (userRole) {
                Role.ADMINISTRADOR -> {
                    add("Cliente" to reservation.nameUser)
                    add("Barbero" to reservation.nameBarber)
                }
                Role.BARBERO -> {
                    add("Cliente" to reservation.nameUser)
                }
                Role.CLIENTE -> {
                    add("Barbero" to reservation.nameBarber)
                }
            }
        }

        // Iteramos sobre la lista para renderizar cada línea
        items.forEach { (label, value) ->
            Text(
                text = buildAnnotatedString {
                    withStyle(style = labelStyle) { append("$label: ") }
                    withStyle(style = valueStyle) { append(value.toString()) }
                },
                fontSize = fontSize
            )
        }
    }
}
