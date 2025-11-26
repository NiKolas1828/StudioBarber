package com.dsmovil.studiobarber.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsmovil.studiobarber.R

@Composable
fun BottomActionBar(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textButton: String
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = colorResource(id = R.color.background_color),
        tonalElevation = 8.dp
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.icon_color_red),
                contentColor = Color.White,
                disabledContainerColor = colorResource(R.color.disabled_button),
                disabledContentColor = Color.White.copy(alpha = 0.5f)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp
            )
        ) {
            Text(
                text = textButton,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}