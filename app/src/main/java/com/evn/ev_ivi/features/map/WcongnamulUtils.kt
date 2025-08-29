package com.evn.ev_ivi.features.map

import kotlin.math.*

data class Wcongnamul(val x: Double, val y: Double)

fun wgs84ToWcongnamul(latitude: Double, longitude: Double): Wcongnamul {
    val centralMeridian = 127.0 * PI / 180
    val falseEasting = 600000.0
    val falseNorthing = 300000.0
    val scaleFactor = 0.9999
    val a = 6378137.0
    val f = 1 / 298.257223563
    val e2 = 2 * f - f * f

    val latRad = latitude * PI / 180
    val lonRad = longitude * PI / 180

    val n = f / (2 - f)
    val A = 1 + n + 5 / 4 * n * n + 5 / 4 * n * n * n
    val B = 3 * n + 3 * n * n + 21 / 8 * n * n * n
    val C = 15 / 8 * n * n + 15 / 8 * n * n * n
    val D = 35 / 24 * n * n * n
    val m =
        a * (1 - e2) * (A * latRad - B * sin(2 * latRad) / 2 + C * sin(4 * latRad) / 4 - D * sin(6 * latRad) / 6)

    val nu = a / sqrt(1 - e2 * sin(latRad) * sin(latRad))
    val t = tan(latRad) * tan(latRad)
    val c = e2 / (1 - e2) * cos(latRad) * cos(latRad)
    val A_ = (lonRad - centralMeridian) * cos(latRad)
    val x =
        scaleFactor * nu * (A_ + (1 - t + c) * A_ * A_ * A_ / 6 + (5 - 18 * t + t * t + 72 * c - 58 * e2) * A_ * A_ * A_ * A_ * A_ / 120) + falseEasting
    val y =
        scaleFactor * (m + nu * tan(latRad) * (A_ * A_ / 2 + (5 - t + 9 * c + 4 * c * c) * A_ * A_ * A_ * A_ / 24 + (61 - 58 * t + t * t + 600 * c - 330 * e2) * A_ * A_ * A_ * A_ * A_ * A_ / 720)) + falseNorthing

    return Wcongnamul(x, y)
}