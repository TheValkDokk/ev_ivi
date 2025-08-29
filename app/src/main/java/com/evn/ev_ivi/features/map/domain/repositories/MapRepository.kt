package com.evn.ev_ivi.features.map.domain.repositories

import com.evn.ev_ivi.features.map.domain.entities.MapLocation

interface MapRepository {
    suspend fun searchLocations(query: String): Result<List<MapLocation>>
}