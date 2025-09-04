package com.evn.ev_ivi.features.map.domain.usecases

import android.location.Location
import com.evn.ev_ivi.features.map.domain.repositories.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

class GetLocationUpdatesUseCase(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(): Flow<Location> = locationRepository.getLocationUpdates()
        // Uncomment to filter by accuracy if needed
        // .filter { it.accuracy < 50f }
}