package com.example.weather_app.prefs

interface PreferenceStorage {
    fun <T> save(key: String, value: T)
    fun getString(key: String, defaultValue: String = ""): String
    fun getInt(key: String, defaultValue: Int = 0): Int
    fun getLong(key: String, defaultValue: Long = 0L): Long
}