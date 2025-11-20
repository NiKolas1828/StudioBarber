package com.dsmovil.studiobarber.di

import com.dsmovil.studiobarber.data.repositories.AuthRepository
import com.dsmovil.studiobarber.data.repositories.AuthRepositoryImpl
import com.dsmovil.studiobarber.domain.usecases.LoginUseCase
import com.dsmovil.studiobarber.domain.usecases.RegisterUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl) : AuthRepository

    companion object {
        @Provides
        @Singleton
        fun provideLoginUseCase(repository: AuthRepository): LoginUseCase {
            return LoginUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideRegisterUseCase(repository: AuthRepository): RegisterUseCase {
            return RegisterUseCase(repository)
        }
    }
}
