package com.dsmovil.studiobarber.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.dsmovil.studiobarber.R
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import com.dsmovil.studiobarber.domain.models.Role
import com.dsmovil.studiobarber.ui.components.auth.AuthTextField

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (Role) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(state) {
        if (state is LoginUiState.Success) {
            onLoginSuccess((state as LoginUiState.Success).user.role)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_color)),
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
                painter = painterResource(id = R.drawable.studio_barber_logo),
                contentDescription = "Studio Barber Logo",
                modifier = Modifier
                    .height(300.dp)
                    .padding(bottom = 32.dp)
            )

            AuthTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                isPasswordField = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state != LoginUiState.Loading,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.button_color),
                    contentColor = Color.White
                )
            ) {
                if (state == LoginUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp))
                } else {
                    Text("Iniciar Sesión")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Forgot password text
            TextButton(
                onClick = { /* Handle password reset */ },
                modifier = Modifier.fillMaxWidth(),
                content = {
                    Text(
                        text = "Olvidaste tu contraseña?",
                        color = colorResource(id = R.color.teal_200),
                        fontWeight = FontWeight.Bold
                    )
                }
            )

            // Error message
            if (state is LoginUiState.Error) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = (state as LoginUiState.Error).message, color = Color.Red)
            }
        }

    }
}
