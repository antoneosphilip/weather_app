package com.example.weather_app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class NoInternetAlertWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (!hasInternetConnection()) {
            showNoInternetNotification()
        }
        return Result.success()
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showNoInternetNotification() {
        val ctx = getLocalizedContext()
        val channelId = "no_internet_channel"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                ctx.getString(R.string.no_internet_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = androidx.core.app.NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(ctx.getString(R.string.no_internet_title))
            .setContentText(ctx.getString(R.string.no_internet_message))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1003, notification)
    }

    private fun getLocalizedContext(): Context {
        val savedLang = applicationContext
            .getSharedPreferences("settings", Context.MODE_PRIVATE)
            .getString("language_code", "en") ?: "en"
        val locale = java.util.Locale(savedLang)
        val config = applicationContext.resources.configuration
        config.setLocale(locale)
        return applicationContext.createConfigurationContext(config)
    }
}