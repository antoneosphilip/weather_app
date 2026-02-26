package com.example.weather_app.presentation.setting.viewModel

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {

    fun setLocale(context: Context, languageCode: String): Context {
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            .edit()
            .putString("language_code", languageCode)
            .apply()

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }

    fun getLanguageCode(language: String): String {
        return when (language) {
            "Arabic", "العربية" -> "ar"
            else -> "en"
        }
    }
}