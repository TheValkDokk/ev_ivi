package com.evn.ev_ivi.features.map.data.models

import com.google.gson.annotations.SerializedName


data class MapLocationModel (
    val id: Long,
    val address: String,
    val longitude: Any? = null,
    val latitude: Any? = null,
    @SerializedName("case_type")
    val caseType: String,
    @SerializedName("reco_method")
    val recoMethod: String,
    @SerializedName("floor_count")
    val floorCount: Long,
    val distance: Any? = null,
    val similarity: Double
)