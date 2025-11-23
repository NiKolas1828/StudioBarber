package com.dsmovil.studiobarber.ui.screens.admin.home

import com.dsmovil.studiobarber.domain.usecases.LogoutUseCase
import com.dsmovil.studiobarber.ui.screens.admin.BaseAdminViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    logoutUseCase: LogoutUseCase
) : BaseAdminViewModel(logoutUseCase) {}