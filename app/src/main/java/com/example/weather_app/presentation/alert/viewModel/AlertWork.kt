package com.example.weather_app

import LocationProvider
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
import kotlin.math.log

class AlertWorker(context: Context, params: WorkerParameters,)
    : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val endTime = inputData.getLong("END_TIME", 0)
        val isNotification = inputData.getBoolean("IS_NOTIFICATION", false)
        val now = System.currentTimeMillis()
        val alertId = inputData.getLong("ALERT_ID", -1L)
         val weatherRepo= (applicationContext as MyApplication).appContainer.weatherRepo
        Log.i("AlertWorker", "doWork fired! isNotification=$isNotification")

        if (now <= endTime) {

            val lat = SharedPreferencesHelper.getInstance(applicationContext).getString("lat","0.0")
            val lon = SharedPreferencesHelper.getInstance(applicationContext).getString("lon","0.0")

            return try {

                val weatherResponse =
                    weatherRepo.getWeather(
                        lat.toDouble(),
                        lon.toDouble(),
                        Constants.apiKey
                    )

                val message = getWeatherAlertMessage(weatherResponse.weather[0].id)

                if (isNotification) {
                    showNotification(message)
                } else {
                    showAlarm(message)
                }
                Log.i("succccc", "s: "+weatherResponse.weather[0].id)
                Log.i("succccc", "s: "+alertId)

                if (alertId != -1L) {
                        Log.i("deleteee", "doWork: ")
                        weatherRepo.deleteAlert(alertId.toInt())
                    }
                Result.success()

            } catch (e: Exception) {
                e.printStackTrace()
                Log.i("errorrr", "error: ")
                Result.retry()
            }


        }

        return Result.success()
    }



    private fun getWeatherAlertMessage(weatherId: Int): String {
        return when (weatherId) {
            in 200..232 -> "⛈️ Thunderstorm alert! Stay indoors."
            in 300..321 -> "🌦️ Drizzle expected. Carry an umbrella!"
            in 500..504 -> "🌧️ Rain alert! Roads may be slippery."
            511 -> "🧊 Freezing rain! Extremely dangerous."
            in 520..531 -> "🌧️ Heavy shower! Expect flooding."
            in 600..622 -> "❄️ Snow alert! Stay safe."
            in 700..781 -> "🌫️ Low visibility! Drive slowly."
            800 -> "☀️ Clear skies! Great weather today."
            in 801..804 -> "☁️ Cloudy weather expected."
            else -> "🌤️ Stay updated on weather conditions."
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
        val channelId = "alert_notification_channel"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Weather Alerts", NotificationManager.IMPORTANCE_HIGH
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
            .setContentTitle("⛈️ Weather Alert")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .addAction(0, "DISMISS", dismissPending)
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