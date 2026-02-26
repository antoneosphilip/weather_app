package com.example.weather_app.presentation.home.viewModel

import android.app.Activity
import android.content.BroadcastReceiver
import com.example.weather_app.data.location.ILocationProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.maps.model.LatLng

class FakeLocationProvider(
    private val fakeLat: Double = 30.0,
    private val fakeLon: Double = 31.0
) : ILocationProvider {

    override fun hasPermission(): Boolean = true

    override suspend fun getLocation(): LatLng? {
        return LatLng(fakeLat, fakeLon)
    }

    override fun checkLocationSettings(
        activity: Activity,
        onSuccess: () -> Unit,
        onResolvable: (ResolvableApiException) -> Unit,
        onFailed: () -> Unit
    ) {

    }

    override fun registerLocationReceiver(
        onLocationEnabled: () -> Unit
    ): BroadcastReceiver? {
        onLocationEnabled()
        return null
    }
}