package com.evn.ev_ivi.core.utils

import android.location.Location
import com.evn.ev_ivi.MainApplication
import com.evn.ev_ivi.features.map.domain.entities.MapLocation
import com.kakaomobility.knsdk.KNSDK
import com.kakaomobility.knsdk.common.objects.KNPOI
import com.kakaomobility.knsdk.common.util.FloatPoint

fun Location.toFloatPoint(): FloatPoint {
    val startLat = latitude
    val startLong = longitude
    val ss = MainApplication.knsdk.convertWGS84ToKATEC(startLong, startLat)
    return FloatPoint(ss.x.toFloat(), ss.y.toFloat())
}

fun Location.toKNPOI() : KNPOI {
    val katecPoint = KNSDK.convertWGS84ToKATEC(longitude, latitude)
    return KNPOI("현재 위치", katecPoint.x.toInt(), katecPoint.y.toInt(), "출발지")
}

fun MapLocation.toFloatPoint(): FloatPoint {
    val ss = MainApplication.knsdk.convertWGS84ToKATEC(lng, lat)
    return FloatPoint(ss.x.toFloat(), ss.y.toFloat())
}
