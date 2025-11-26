package com.dsmovil.studiobarber.ui.components.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.dsmovil.studiobarber.R
import com.dsmovil.studiobarber.ui.components.ActionButtons

@Composable
fun AdminItemCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconColor: Color,
    iconBackgroundColor: Color,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    textContent: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CardInfo(
                modifier = Modifier.weight(1f),
                icon = icon,
                iconColor = iconColor,
                iconBackgroundColor = iconBackgroundColor,
                textContent = textContent
            )

            ActionButtons(
                firstIcon = ImageVector.vectorResource(id = R.drawable.ic_edit),
                onFirstClick = onEditClick,
                firstIconTint = colorResource(id = R.color.icon_color_red),
                secondIcon = ImageVector.vectorResource(id = R.drawable.ic_trash),
                onSecondClick = onDeleteClick,
                secondIconTint = colorResource(id = R.color.icon_color_red),
                firstContentDescription = "Editar",
                secondContentDescription = "Eliminar"
            )
        }
    }
}

@Composable
private fun CardInfo(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconColor: Color,
    iconBackgroundColor: Color,
    textContent: @Composable ColumnScope.() -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(iconBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "icono",
                tint = iconColor,
                modifier = Modifier.size(32.dp)
            )
        }

        Column(
            content = textContent
        )
    }
}