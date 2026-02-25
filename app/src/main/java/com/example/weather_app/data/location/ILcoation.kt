package com.example.weather_app.data.location

import android.content.BroadcastReceiver
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.maps.model.LatLng

interface ILocationProvider {
    fun hasPermission(): Boolean
    suspend fun getLocation(): LatLng?
    fun checkLocationSettings(
        activity: android.app.Activity,
        onSuccess: () -> Unit,
        onResolvable: (ResolvableApiException) -> Unit,
        onFailed: () -> Unit
    )
    fun registerLocationReceiver(onLocationEnabled: () -> Unit): BroadcastReceiver?
}