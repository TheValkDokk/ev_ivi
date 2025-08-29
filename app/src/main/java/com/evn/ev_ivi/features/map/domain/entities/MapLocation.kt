package com.evn.ev_ivi.features.map.domain.entities

data class MapLocation(
    val id: Long,
    val address: String,
    val longitude: Double?,
    val latitude: Double?,
    val caseType: String,
    val recoMethod: String,
    val floorCount: Long,
    val distance: Double?,
    val similarity: Double
)