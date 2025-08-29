package com.evn.ev_ivi.features.map.data.models

import com.google.gson.annotations.SerializedName


data class MapLocationModel (
    val id: Long,
    val name: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val x: Double,
    val y: Double,
    @SerializedName("building_x")
    val buildingX: Double,
    @SerializedName("building_y")
    val buildingY: Double,
    @SerializedName("building_lng")
    val buildingLng: Double,
    @SerializedName("building_lat")
    val buildingLat: Double,
    @SerializedName("entrance_x")
    val entranceX: Double,
    @SerializedName("entrance_y")
    val entranceY: Double,
    @SerializedName("entrance_lng")
    val entranceLng: Double,
    @SerializedName("entrance_lat")
    val entranceLat: Double,
    @SerializedName("near_r_x")
    val nearRX: Double,
    @SerializedName("near_r_y")
    val nearRY: Double,
    @SerializedName("near_r_lng")
    val nearRLng: Double,
    @SerializedName("near_r_lat")
    val nearRLat: Double,
    @SerializedName("near_rv_x")
    val nearRvX: Double,
    @SerializedName("near_rv_y")
    val nearRvY: Double,
    @SerializedName("near_rv_lng")
    val nearRvLng: Double,
    @SerializedName("near_rv_lat")
    val nearRvLat: Double,
    @SerializedName("reco_x")
    val recoX: Double,
    @SerializedName("reco_y")
    val recoY: Double,
    @SerializedName("reco_lng")
    val recoLng: Double,
    @SerializedName("reco_lat")
    val recoLat: Double,
    @SerializedName("near_r")
    val nearR: Double,
    @SerializedName("near_rv")
    val nearRv: Double?,
    val reco: String,
    @SerializedName("case_type")
    val caseType: String,
    @SerializedName("floor_count")
    val floorCount: Long,
    val distance: Double?,
    val similarity: Double,
    @SerializedName("csv_upload_batch")
    val csvUploadBatch: String,
    val created: String,
    val modified: String,
    @SerializedName("building_code")
    val buildingCode: Long
)