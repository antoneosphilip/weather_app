package com.example.weather_app.prefs


import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesHelper private constructor(context: Context):PreferenceStorage {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "weather_app_prefs"

        @Volatile
        private var INSTANCE: SharedPreferencesHelper? = null

        fun getInstance(context: Context): SharedPreferencesHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SharedPreferencesHelper(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

   override fun <T> save(key: String, value: T) {
        val editor = prefs.edit()
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            is Double -> editor.putString(key, value.toString())
            else -> editor.putString(key, gson.toJson(value))
        }
        editor.apply()
    }

    override fun getString(key: String, defaultValue: String): String {
        return prefs.getString(key, defaultValue) ?: defaultValue
    }

    override fun getInt(key: String, defaultValue: Int ): Int {
        return prefs.getInt(key, defaultValue)
    }


    override fun getLong(key: String, defaultValue: Long ): Long {
        return prefs.getLong(key, defaultValue)
    }

}