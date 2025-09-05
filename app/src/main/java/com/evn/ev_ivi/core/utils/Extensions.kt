package com.evn.ev_ivi.core.utils

import android.location.Location
import com.evn.ev_ivi.MainApplication
import com.kakaomobility.knsdk.common.util.FloatPoint

fun Location.toFloatPoint(): FloatPoint {
    val startLat = latitude
    val startLong = longitude
    val ss = MainApplication.knsdk.convertWGS84ToKATEC(startLong, startLat)
    return FloatPoint(ss.x.toFloat(), ss.y.toFloat())
}
