package com.dsmovil.studiobarber.ui.screens.utils

import com.dsmovil.studiobarber.domain.usecases.LogoutUseCase
import com.dsmovil.studiobarber.utils.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val logoutUseCase = mockk<LogoutUseCase>(relaxed = true)
    private lateinit var viewModel: BaseViewModel

    @Test
    fun `onLogout calls logoutUseCase and onLogoutSuccess`() = runTest {
        viewModel = BaseViewModel(logoutUseCase)
        val onLogoutSuccess = mockk<() -> Unit>(relaxed = true)

        viewModel.onLogout(onLogoutSuccess)

        verify { logoutUseCase() }
        verify { onLogoutSuccess() }
    }
}
