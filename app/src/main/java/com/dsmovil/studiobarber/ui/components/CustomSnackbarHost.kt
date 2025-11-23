package com.dsmovil.studiobarber.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.dsmovil.studiobarber.R

@Composable
fun CustomSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier
    ) { data ->
        val isError = data.visuals.actionLabel == "ERROR_TYPE"

        val containerColor = if (isError) colorResource(id = R.color.message_error) else colorResource(id = R.color.message_success)

        Snackbar(
            modifier = Modifier.padding(16.dp),
            containerColor = containerColor,
            contentColor = Color.White,
            shape = RoundedCornerShape(8.dp),
            dismissAction = if (data.visuals.withDismissAction) {
                @Composable {
                    IconButton(
                        onClick = { data.dismiss() },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = Color.White
                            )
                        }
                    )
                }
            } else null
        ) {
            Text(text = data.visuals.message)
        }
    }
}