package com.example.favoriteplaces.di

import android.content.Context
import com.example.favoriteplaces.usecase.NearbySearchInteractor
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Singleton
    @Provides
    fun providePlacesClient(@ApplicationContext context: Context): PlacesClient =
        Places.createClient(context)

    @Singleton
    @Provides
    fun bindAnalyticsService(
        placesClient: PlacesClient
    ) = NearbySearchInteractor(placesClient)
}