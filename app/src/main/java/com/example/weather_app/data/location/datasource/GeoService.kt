package com.example.weather_app.data.location.datasource

import com.example.weather_app.data.location.model.GeoLocation
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoService {
    @GET("direct")
    suspend fun searchLocations(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") apiKey: String
    ): List<GeoLocation>
}