package com.example.weather_app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weather_app.constant.Constants
import com.example.weather_app.prefs.SharedPreferencesHelper
import java.util.Locale

class AlertWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val startTime = inputData.getLong("START_TIME", 0)
        val isNotification = inputData.getBoolean("IS_NOTIFICATION", false)
        val alertId = inputData.getLong("ALERT_ID", -1L)
        val weatherRepo = (applicationContext as MyApplication).appContainer.weatherRepo
        val now = System.currentTimeMillis()

        if (now < startTime) {
            return Result.success()
        }

        val lat = SharedPreferencesHelper.getInstance(applicationContext).getString("lat", "0.0")
        val lon = SharedPreferencesHelper.getInstance(applicationContext).getString("lon", "0.0")

        return try {
            val weatherResponse = weatherRepo.getWeather(
                lat.toDouble(),
                lon.toDouble(),
                Constants.apiKey
            )
            val message = getWeatherAlertMessage(getLocalizedContext(), weatherResponse.weather[0].id)
            if (isNotification) showNotification(message) else showAlarm(message)
            if (alertId != -1L) weatherRepo.deleteAlert(alertId.toInt())
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private fun getLocalizedContext(): Context {
        val savedLang = applicationContext
            .getSharedPreferences("settings", Context.MODE_PRIVATE)
            .getString("language_code", "en") ?: "en"
        val localeCode = if (savedLang == "Arabic" || savedLang == "ar") "ar" else "en"
        val locale = Locale(localeCode)
        val config = applicationContext.resources.configuration
        config.setLocale(locale)
        return applicationContext.createConfigurationContext(config)
    }

    private fun getWeatherAlertMessage(context: Context, weatherId: Int): String {
        return when (weatherId) {
            in 200..232 -> context.getString(R.string.weather_alert_thunderstorm)
            in 300..321 -> context.getString(R.string.weather_alert_drizzle)
            in 500..504 -> context.getString(R.string.weather_alert_rain)
            511         -> context.getString(R.string.weather_alert_freezing_rain)
            in 520..531 -> context.getString(R.string.weather_alert_heavy_shower)
            in 600..622 -> context.getString(R.string.weather_alert_snow)
            in 700..781 -> context.getString(R.string.weather_alert_low_visibility)
            800         -> context.getString(R.string.weather_alert_clear)
            in 801..804 -> context.getString(R.string.weather_alert_cloudy)
            else        -> context.getString(R.string.weather_alert_default)
        }
    }

    private fun showAlarm(message: String) {
        val intent = Intent(applicationContext, AlertOverlayService::class.java).apply {
            putExtra("WEATHER_MESSAGE", message)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            applicationContext.startForegroundService(intent)
        } else {
            applicationContext.startService(intent)
        }
    }

    private fun showNotification(message: String) {
        val ctx = getLocalizedContext()
        val channelId = "alert_notification_channel"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                ctx.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(
                    android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION),
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                )
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val dismissIntent = Intent(applicationContext, AlarmDismissReceiver::class.java)
        val dismissPending = PendingIntent.getBroadcast(
            applicationContext, 0, dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(ctx.getString(R.string.notification_title))
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .addAction(0, ctx.getString(R.string.dismiss), dismissPending)
            .build()

        notificationManager.notify(1001, notification)
    }
}

class AlarmDismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1001)
    }
}