package com.dsmovil.studiobarber.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.dsmovil.studiobarber.R

@Composable
fun LogoutButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(56.dp)
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_exit),
            contentDescription = "Cerrar Sesión",
            tint = colorResource(R.color.icon_color_red),
        )
    }
}