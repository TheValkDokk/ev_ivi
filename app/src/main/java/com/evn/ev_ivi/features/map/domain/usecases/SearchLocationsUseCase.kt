package com.evn.ev_ivi.features.map.domain.usecases

import com.evn.ev_ivi.features.map.domain.entities.MapLocation
import com.evn.ev_ivi.features.map.domain.repositories.MapRepository

class SearchLocationsUseCase(
    private val mapRepository: MapRepository
) {
    suspend operator fun invoke(query: String): Result<List<MapLocation>> {
        if (query.isBlank()) {
            return Result.success(emptyList())
        }
        return mapRepository.searchLocations(query.trim())
    }
}