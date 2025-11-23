package com.dsmovil.studiobarber

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.dsmovil.studiobarber.ui.screens.navigation.StudioBarberNavGraph
import com.dsmovil.studiobarber.ui.theme.StudioBarberTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            StudioBarberTheme {
                val navController = rememberNavController()

                StudioBarberNavGraph(navController)
            }
        }
    }
}