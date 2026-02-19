package com.example.weather_app.presentation.favorite.view

fun String.extractCountry(): String {
    return this
        .removeSuffix(")")
        .substringAfterLast(",")
        .trim()
}

fun String.extractCityPart(): String {
    val cleaned = this.removeSuffix(")")
    val parts = cleaned.split(",").map { it.trim() }

    if (parts.isEmpty()) return ""

    val withoutCountry = parts.dropLast(1)

    val finalParts =
        if (withoutCountry.isNotEmpty() && withoutCountry[0].contains("+"))
            withoutCountry.drop(1)
        else
            withoutCountry

    return finalParts.joinToString(", ")
}
