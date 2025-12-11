package com.dsmovil.studiobarber.di

import com.dsmovil.studiobarber.BuildConfig
import com.dsmovil.studiobarber.data.remote.AuthInterceptor
import com.dsmovil.studiobarber.data.remote.auth.AuthApiService
import com.dsmovil.studiobarber.data.remote.barber.BarberApiService
import com.dsmovil.studiobarber.data.remote.reservation.ReservationApiService
import com.dsmovil.studiobarber.data.remote.timeblock.TimeBlockApiService
import com.dsmovil.studiobarber.data.remote.service.ServiceApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return interceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor, authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBarberApiService(retrofit: Retrofit): BarberApiService {
        return retrofit.create(BarberApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideReservationApiService(retrofit: Retrofit): ReservationApiService {
        return retrofit.create(ReservationApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTimeBlockApiService(retrofit: Retrofit): TimeBlockApiService {
        return retrofit.create(TimeBlockApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideServiceApiService(retrofit: Retrofit): ServiceApiService {
        return retrofit.create(ServiceApiService::class.java)
    }
}