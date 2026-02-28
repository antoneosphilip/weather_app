package com.example.weather_app.presentation.home.view

fun formatNumber(number: Int, context: android.content.Context): String {
    val savedLang = context.getSharedPreferences("settings", android.content.Context.MODE_PRIVATE)
        .getString("language_code", "en") ?: "en"
    return if (savedLang == "ar") {
        number.toString()
            .replace('0', '٠').replace('1', '١').replace('2', '٢')
            .replace('3', '٣').replace('4', '٤').replace('5', '٥')
            .replace('6', '٦').replace('7', '٧').replace('8', '٨')
            .replace('9', '٩')
    } else {
        number.toString()
    }
}