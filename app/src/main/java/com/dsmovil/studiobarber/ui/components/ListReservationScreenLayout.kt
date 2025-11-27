package com.dsmovil.studiobarber.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.dsmovil.studiobarber.R

@Composable
fun ListReservationScreenLayout(
    onNavigateToHome: () -> Unit = {},
    onLogout: () -> Unit,
    snackbarHost: @Composable () -> Unit = {},
    footerText: String,
    content: @Composable () -> Unit,
){
    Scaffold(
        containerColor = colorResource(R.color.background_color),
        snackbarHost = snackbarHost
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ){
            Column(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
            ){
                Spacer(modifier = Modifier.height(8.dp))

                ReservationHeader{
                    Button(
                        onClick = onNavigateToHome,
                        modifier = Modifier
                            .width(150.dp)
                            .height(45.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.icon_color_red),
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Text("Inicio")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                content()
            }

            Spacer(modifier = Modifier.height(16.dp))

            Footer(
                message = footerText,
                modifier = Modifier
                    .fillMaxWidth(),
                actions = { LogoutButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.Start)
                )},
            )
        }
    }
}
