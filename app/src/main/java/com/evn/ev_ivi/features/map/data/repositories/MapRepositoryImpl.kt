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
        name = this.name,
        address = this.address,
        lat = this.lat,
        lng = this.lng,
        x = this.x,
        y = this.y,
        buildingX = this.buildingX,
        buildingY = this.buildingY,
        buildingLng = this.buildingLng,
        buildingLat = this.buildingLat,
        entranceX = this.entranceX,
        entranceY = this.entranceY,
        entranceLng = this.entranceLng,
        entranceLat = this.entranceLat,
        nearRX = this.nearRX,
        nearRY = this.nearRY,
        nearRLng = this.nearRLng,
        nearRLat = this.nearRLat,
        nearRvX = this.nearRvX,
        nearRvY = this.nearRvY,
        nearRvLng = this.nearRvLng,
        nearRvLat = this.nearRvLat,
        recoX = this.recoX,
        recoY = this.recoY,
        recoLng = this.recoLng,
        recoLat = this.recoLat,
        nearR = this.nearR,
        nearRv = this.nearRv,
        reco = this.reco,
        caseType = this.caseType,
        floorCount = this.floorCount,
        distance = this.distance,
        similarity = this.similarity,
        csvUploadBatch = this.csvUploadBatch,
        created = this.created,
        modified = this.modified,
        buildingCode = this.buildingCode
    )
}