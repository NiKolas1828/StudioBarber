package com.dsmovil.studiobarber.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ActionButtons(
    firstIcon: ImageVector,
    secondIcon: ImageVector,
    onFirstClick: () -> Unit,
    onSecondClick: () -> Unit,
    modifier: Modifier = Modifier,
    firstIconTint: Color = Color(0xFFF44336),
    secondIconTint: Color = Color(0xFFF44336)
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy((-10).dp)
    ) {
        IconButton(onClick = onFirstClick) {
            Icon(
                imageVector = firstIcon,
                contentDescription = null,
                tint = firstIconTint
            )
        }

        IconButton(onClick = onSecondClick) {
            Icon(
                imageVector = secondIcon,
                contentDescription = null,
                tint = secondIconTint
            )
        }
    }
}