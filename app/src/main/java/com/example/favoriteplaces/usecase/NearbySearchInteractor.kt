package com.example.favoriteplaces.usecase

import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.LocationBias
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.ktx.api.net.awaitFindAutocompletePredictions
import com.google.android.libraries.places.ktx.api.net.findAutocompletePredictionsRequest
import javax.inject.Inject

class NearbySearchInteractor @Inject constructor(
    private val placesClient: PlacesClient
) {
    suspend fun getPlaces(query: String): MutableList<AutocompletePrediction> {
        val bias: LocationBias = RectangularBounds.newInstance(
            LatLng(37.7576948, -122.4727051), // SW lat, lng
            LatLng(37.808300, -122.391338) // NE lat, lng
        )

        val request = findAutocompletePredictionsRequest {
            locationBias = bias
            typeFilter = TypeFilter.ESTABLISHMENT
            this.query = query
            countries = listOf("US")
        }

        val response = placesClient
            .awaitFindAutocompletePredictions(request)

        return response.autocompletePredictions
    }
}