package com.dsmovil.studiobarber.ui.screens.admin.home

import com.dsmovil.studiobarber.domain.usecases.LogoutUseCase
import com.dsmovil.studiobarber.ui.screens.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    logoutUseCase: LogoutUseCase
) : BaseViewModel(logoutUseCase) {}