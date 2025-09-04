package com.evn.ev_ivi.features.map.data.datasource.remote

import com.evn.ev_ivi.features.map.data.models.MapLocationResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface MapSearchApi {
    @GET("/api/map/parking/search")
    suspend fun search(@Query("query") query: String): MapLocationResponseModel
}