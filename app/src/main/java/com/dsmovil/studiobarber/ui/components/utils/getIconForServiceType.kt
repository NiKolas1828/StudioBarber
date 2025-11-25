package com.dsmovil.studiobarber.ui.components.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.dsmovil.studiobarber.R
import com.dsmovil.studiobarber.domain.models.ServiceType

@Composable
fun getIconForServiceType(
    type: ServiceType
): ImageVector {
    return when (type) {
        ServiceType.HAIRCUT -> ImageVector.vectorResource(id = R.drawable.ic_haircut)
        ServiceType.BEARD -> ImageVector.vectorResource(id = R.drawable.ic_beard)
        ServiceType.OTHER -> ImageVector.vectorResource(id = R.drawable.ic_services)
    }
}