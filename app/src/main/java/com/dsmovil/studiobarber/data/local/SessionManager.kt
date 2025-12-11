package com.dsmovil.studiobarber.data.local

import com.dsmovil.studiobarber.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    private var authToken: String? = null

    fun saveUser(user: User, token: String? = null) {
        _currentUser.value = user
        authToken = token
    }

    fun logout() {
        _currentUser.value = null
        authToken = null
    }

    fun isLoggedIn(): Boolean {
        return _currentUser.value != null
    }

    fun getToken(): String? = authToken

    fun getCurrentUserId(): Long? = _currentUser.value?.id

    fun getCurrentUsername(): String? = _currentUser.value?.name
}