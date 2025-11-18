package com.dsmovil.studiobarber.ui.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.dsmovil.studiobarber.R // Make sure to place the logo in your drawable resources
import androidx.compose.ui.res.colorResource



@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegisterSuccess: () -> Unit
) {
    val state = viewModel.uiState

    if (state.success) {
        onRegisterSuccess()
    }
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
            // Firstname text field
            OutlinedTextField(
                value = state.firstname,
                onValueChange = { viewModel.onFirstNameChange(it) },
                label = { Text("Firstname") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    cursorColor = Color.White,
                    focusedBorderColor = colorResource(id = R.color.teal_200),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    focusedPlaceholderColor = Color.LightGray,
                    unfocusedPlaceholderColor = Color.Gray
                ))
            Spacer(modifier = Modifier.height(16.dp))


            // Firstname text field
            OutlinedTextField(
                value = state.lastname,
                onValueChange = { viewModel.onLastNameChange(it) },
                label = { Text("LastName") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    cursorColor = Color.White,
                    focusedBorderColor = colorResource(id = R.color.teal_200),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    focusedPlaceholderColor = Color.LightGray,
                    unfocusedPlaceholderColor = Color.Gray
                ))
            Spacer(modifier = Modifier.height(16.dp))


            // Phone text field
            OutlinedTextField(
                value = state.phone,
                onValueChange = { viewModel.onPhoneChange(it) },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    cursorColor = Color.White,
                    focusedBorderColor = colorResource(id = R.color.teal_200),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    focusedPlaceholderColor = Color.LightGray,
                    unfocusedPlaceholderColor = Color.Gray
                ))

            Spacer(modifier = Modifier.height(16.dp))

            // Email text field
            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    cursorColor = Color.White,
                    focusedBorderColor = colorResource(id = R.color.teal_200),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    focusedPlaceholderColor = Color.LightGray,
                    unfocusedPlaceholderColor = Color.Gray
                ))

            Spacer(modifier = Modifier.height(16.dp))


            // Password text field
            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    cursorColor = Color.White,
                    focusedBorderColor = colorResource(id = R.color.teal_200),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    focusedPlaceholderColor = Color.LightGray,
                    unfocusedPlaceholderColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(16.dp))



            // confirmPassword text field
            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChange(it) },
                label = { Text("confirmPassword") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    cursorColor = Color.White,
                    focusedBorderColor = colorResource(id = R.color.teal_200),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    focusedPlaceholderColor = Color.LightGray,
                    unfocusedPlaceholderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Register Button
            Button(
                onClick = { viewModel.register() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.loading,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.button_color), // Use colorResource to get the color
                    contentColor = Color.White // The color for the text inside the button
                )            ) {
                if (state.loading) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp))
                } else {
                    Text("Registrarse")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Forgot password text
            TextButton(
                onClick = { /* Handle password reset */ },
                modifier = Modifier.fillMaxWidth(),
                content = {
                    Text(
                        text = "Ya tienes cuenta",
                        color = colorResource(id = R.color.teal_200),
                        fontWeight = FontWeight.Bold
                    )
                }
            )

            // Error message
            if (state.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = state.error, color = Color.Red)
            }
        }

    }
}
