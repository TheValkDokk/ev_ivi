package com.evn.ev_ivi.features.map.data.datasource.remote

import com.evn.ev_ivi.features.map.data.models.MapLocationModel
import com.evn.ev_ivi.features.map.data.models.MapLocationResponseModel
import com.evn.ev_ivi.features.map.data.models.SearchRequestModel

interface MapSearchDatasource {
    suspend fun search(request: SearchRequestModel): List<MapLocationModel>
}

class MapSearchDatasourceImpl (
    private val api: MapSearchApi
) : MapSearchDatasource {
    override suspend fun search(request: SearchRequestModel): List<MapLocationModel> {
        return try {
            val response = api.search(request)
            response.results
        } catch (e: Exception) {
            emptyList()
        }
    }
}