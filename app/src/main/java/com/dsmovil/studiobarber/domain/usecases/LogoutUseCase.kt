package com.dsmovil.studiobarber.domain.usecases

import com.dsmovil.studiobarber.data.local.SessionManager
import com.dsmovil.studiobarber.domain.repositories.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val sessionManager: SessionManager
) {
    operator fun invoke() {
        sessionManager.logout()
    }
}