package com.dsmovil.studiobarber.di

import com.dsmovil.studiobarber.domain.repositories.AuthRepository
import com.dsmovil.studiobarber.data.repositories.AuthRepositoryImpl
import com.dsmovil.studiobarber.data.repositories.BarberRepositoryImpl
import com.dsmovil.studiobarber.data.repositories.ServiceRepositoryImpl
import com.dsmovil.studiobarber.domain.repositories.BarberRepository
import com.dsmovil.studiobarber.domain.repositories.ServiceRepository
import com.dsmovil.studiobarber.domain.usecases.admin.barbers.DeleteBarberUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.barbers.GetBarbersUseCase
import com.dsmovil.studiobarber.data.repositories.ReservationRepositoryImpl
import com.dsmovil.studiobarber.domain.repositories.ReservationRepository
import com.dsmovil.studiobarber.domain.usecases.LoginUseCase
import com.dsmovil.studiobarber.domain.usecases.RegisterUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.barbers.AddBarberUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.barbers.UpdateBarberUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.services.AddServiceUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.services.DeleteServiceUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.services.GetServicesUseCase
import com.dsmovil.studiobarber.domain.usecases.admin.services.UpdateServiceUseCase
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
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindBarberRepository(barberRepositoryImpl: BarberRepositoryImpl): BarberRepository

    @Binds
    @Singleton
    abstract fun bindServiceRepository(serviceRepositoryImpl: ServiceRepositoryImpl): ServiceRepository

    @Binds
    @Singleton
    abstract fun bindReservationRepository(reservationRepositoryImpl: ReservationRepositoryImpl) : ReservationRepository

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

        @Provides
        @Singleton
        fun provideGetBarbersUseCase(repository: BarberRepository): GetBarbersUseCase {
            return GetBarbersUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideDeleteBarberUseCase(repository: BarberRepository): DeleteBarberUseCase {
            return DeleteBarberUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideUpdateBarberUseCase(repository: BarberRepository): UpdateBarberUseCase {
            return UpdateBarberUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideAddBarberUseCase(repository: BarberRepository): AddBarberUseCase {
            return AddBarberUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideGetServicesUseCase(repository: ServiceRepository): GetServicesUseCase {
            return GetServicesUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideAddServiceUseCase(repository: ServiceRepository): AddServiceUseCase {
            return AddServiceUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideUpdateServiceUseCase(repository: ServiceRepository): UpdateServiceUseCase {
            return UpdateServiceUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideDeleteServiceUseCase(repository: ServiceRepository): DeleteServiceUseCase {
            return DeleteServiceUseCase(repository)
        }
    }
}
