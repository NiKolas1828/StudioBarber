package com.dsmovil.studiobarber.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.dsmovil.studiobarber.ui.screens.client.home.SelectOptions

@Composable
fun CreateReservationScreenLayout(
    selectedOption: SelectOptions,
    onChangeOption: (SelectOptions) -> Unit,
    isContinueEnabled: Boolean,
    onContinueClick: () -> Unit,
    onNavigateToReservations: () -> Unit = {},
    onLogout: () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        containerColor = colorResource(id = R.color.background_color),
        snackbarHost = snackbarHost
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 140.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    ReservationHeader{
                        Button(
                            onClick = onNavigateToReservations,
                            modifier = Modifier
                                .width(150.dp)
                                .height(45.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.icon_color_blue),
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            Text("Mis Reservas")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ReservationOptionSelector(
                        selected = selectedOption,
                        onSelectedChange = onChangeOption
                    )

                    content()
                }

                BottomActionBar(
                    enabled = isContinueEnabled,
                    onClick = onContinueClick,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 70.dp),
                    textButton = "Continuar"
                )

                LogoutButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                )
            }
        }
    }
}

