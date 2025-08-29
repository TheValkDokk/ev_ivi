package com.evn.ev_ivi.features.map.data.repositories

import com.evn.ev_ivi.features.map.data.datasource.remote.MapSearchDatasource
import com.evn.ev_ivi.features.map.data.models.SearchRequestModel
import com.evn.ev_ivi.features.map.domain.entities.MapLocation
import com.evn.ev_ivi.features.map.domain.repositories.MapRepository

class MapRepositoryImpl(
    private val mapSearchDatasource: MapSearchDatasource
) : MapRepository {
    
    override suspend fun searchLocations(query: String): Result<List<MapLocation>> {
        return try {
            val request = SearchRequestModel(query)
            val dataModels = mapSearchDatasource.search(request)
            val domainEntities = dataModels.map { it.toDomainEntity() }
            Result.success(domainEntities)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

private fun com.evn.ev_ivi.features.map.data.models.MapLocationModel.toDomainEntity(): MapLocation {
    return MapLocation(
        id = this.id,
        address = this.address,
        longitude = this.longitude as? Double,
        latitude = this.latitude as? Double,
        caseType = this.caseType,
        recoMethod = this.recoMethod,
        floorCount = this.floorCount,
        distance = this.distance as? Double,
        similarity = this.similarity
    )
}