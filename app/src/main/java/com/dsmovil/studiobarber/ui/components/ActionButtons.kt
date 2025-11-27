package com.dsmovil.studiobarber.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.dsmovil.studiobarber.R

@Composable
fun ActionButtons(
    firstIcon: ImageVector,
    secondIcon: ImageVector,
    onFirstClick: (() -> Unit)?,
    onSecondClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    firstIconTint: Color = colorResource(id = R.color.icon_color_red),
    secondIconTint: Color = colorResource(id = R.color.icon_color_red),
    firstContentDescription: String,
    secondContentDescription: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy((-5).dp)
    ) {
        if (onFirstClick != null) {
            IconButton(modifier = Modifier.size(36.dp), onClick = onFirstClick) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = firstIcon,
                    contentDescription = firstContentDescription,
                    tint = firstIconTint
                )
            }
        }

        if (onSecondClick != null) {
            IconButton(modifier = Modifier.size(36.dp), onClick = onSecondClick) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = secondIcon,
                    contentDescription = secondContentDescription,
                    tint = secondIconTint
                )
            }
        }
    }
}