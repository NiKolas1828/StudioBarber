package com.dsmovil.studiobarber.domain.usecases

import com.dsmovil.studiobarber.data.local.SessionManager
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val sessionManager: SessionManager
) {
    operator fun invoke() {
        sessionManager.logout()
    }
}