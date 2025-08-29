package com.evn.ev_ivi.features.map.data.datasource.remote

import com.evn.ev_ivi.features.map.data.models.MapLocationModel
import com.evn.ev_ivi.features.map.data.models.MapLocationResponseModel
import com.evn.ev_ivi.features.map.data.models.SearchRequestModel
import retrofit2.http.Body
import retrofit2.http.POST

interface MapSearchApi {
    @POST("/api/map/parking/search")
    suspend fun search(@Body request: SearchRequestModel): MapLocationResponseModel
}