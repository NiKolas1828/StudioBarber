package com.dsmovil.studiobarber.ui.components

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp

@Composable
fun ListReservationScreenLayout(
    userName: String,
    onNavigateToHome: () -> Unit = {},
    onLogout: () -> Unit,
    snackbarHost: @Composable () -> Unit = {},
    footerText: String,
    content: @Composable () -> Unit,
){
    Scaffold(
        containerColor = Color(0xFF1E1E1E),
        snackbarHost = snackbarHost
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ){
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp)
                    .align(Alignment.TopStart)
                ){
                    Spacer(modifier = Modifier.height(8.dp))

                    ClientHeader(
                        userName = userName,
                    ){
                        Button(
                            onClick = onNavigateToHome,
                            modifier = Modifier
                                .width(150.dp)
                                .height(45.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF44336),
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

                Footer(
                    message = footerText,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
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
}
