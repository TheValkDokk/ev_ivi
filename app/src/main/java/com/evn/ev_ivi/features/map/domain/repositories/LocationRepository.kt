package com.evn.ev_ivi.features.map.domain.repositories

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocationUpdates(): Flow<Location>
}