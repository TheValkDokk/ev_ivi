package com.evn.ev_ivi.features.map.data.models


data class MapLocationModel (
    val id: Long,
    val address: String,
    val longitude: Any? = null,
    val latitude: Any? = null,
    val caseType: String,
    val recoMethod: String,
    val floorCount: Long,
    val distance: Any? = null,
    val similarity: Double
)