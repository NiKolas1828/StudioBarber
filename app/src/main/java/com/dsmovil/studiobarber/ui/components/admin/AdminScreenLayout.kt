package com.dsmovil.studiobarber.ui.components.admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.dsmovil.studiobarber.R
import com.dsmovil.studiobarber.ui.components.LogoutButton
import com.dsmovil.studiobarber.ui.screens.admin.BaseAdminViewModel

@Composable
fun AdminScreenLayout(
    viewModel: BaseAdminViewModel,
    onLogoutSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        containerColor = colorResource(id = R.color.background_color),
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = FabPosition.Center,
        snackbarHost = snackbarHost
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                content()
            }

            Spacer(modifier = Modifier.height(36.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                LogoutButton(
                    modifier = Modifier
                        .align(Alignment.BottomStart),
                    onClick = {
                        viewModel.onLogout(onLogoutSuccess)
                    }
                )
            }
        }
    }
}