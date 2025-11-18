package com.dsmovil.studiobarber.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dsmovil.studiobarber.R

@Composable
fun AuthChooserScreen(
    goToLogin: () -> Unit,
    goToRegister: () -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_color)), // Set your desired background color
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Logo at the top
            Image(
                painter = painterResource(id = R.drawable.studio_barber_logo), // Make sure to add the logo in drawable resources
                contentDescription = "Studio Barber Logo",
                modifier = Modifier
                    .height(300.dp) // Adjust height as necessary
                    .padding(bottom = 32.dp)
            )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = goToLogin,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.button_color), // Use colorResource to get the color
                contentColor = Color.White // The color for the text inside the button
            )
        ) {
            Text("Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = goToRegister,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.button_color), // Use colorResource to get the color
            contentColor = Color.White // The color for the text inside the button
        )
            ) {
            Text("Registrarme")
        }
    }
    }
}
