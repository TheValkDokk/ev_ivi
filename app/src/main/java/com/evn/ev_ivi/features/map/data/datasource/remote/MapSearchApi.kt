package com.evn.ev_ivi.features.map.data.datasource.remote

import com.evn.ev_ivi.features.map.data.models.MapLocationResponseModel
import com.evn.ev_ivi.features.map.data.models.SearchRequestModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface MapSearchApi {
    @GET("/api/map/parking/search")
    suspend fun search(@Query("query") query: String): MapLocationResponseModel
}