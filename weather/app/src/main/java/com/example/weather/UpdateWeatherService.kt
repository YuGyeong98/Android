package com.example.weather

import android.Manifest
import android.app.*
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices

class UpdateWeatherService : Service() {
    private lateinit var appWidgetManager: AppWidgetManager
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        appWidgetManager = AppWidgetManager.getInstance(this)

        createNotificationChannel()
        startForeground(1, createNotification())

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            updateAppWidget(REQUEST_SETTING_ACTIVITY, "권한 없음", "")
            stopSelf()

            return super.onStartCommand(intent, flags, startId)
        }
        LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener { location ->
            WeatherRepository.getVillageForecast(
                serviceKey = getString(R.string.weather_service_key),
                latitude = location.latitude,
                longitude = location.longitude,
                successCallback = { forecastList ->
                    val currentForecast = forecastList.first()
                    updateAppWidget(
                        REQUEST_WEATHER_INFO,
                        getString(R.string.temperature_text, currentForecast.tmp),
                        currentForecast.weather
                    )
                    stopSelf()
                },
                failureCallback = {
                    updateAppWidget(REQUEST_WEATHER_INFO, "에러", "")
                    stopSelf()
                }
            )
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun updateAppWidget(
        requestCode: Int,
        temperature: CharSequence,
        weather: CharSequence
    ) {
        val pendingIntent = if (requestCode == REQUEST_WEATHER_INFO) {
            Intent(this, UpdateWeatherService::class.java).let {
                PendingIntent.getService(this, requestCode, it, PendingIntent.FLAG_IMMUTABLE)
            }
        } else {
            Intent(this, SettingActivity::class.java).let {
                PendingIntent.getActivity(this, requestCode, it, PendingIntent.FLAG_IMMUTABLE)
            }
        }
        val views = RemoteViews(packageName, R.layout.widget_weather).apply {
            setTextViewText(R.id.temperatureTextView, temperature)
            setTextViewText(R.id.weatherTextView, weather)
            setOnClickPendingIntent(R.id.widgetLinearLayout, pendingIntent)
        }
        val provider = ComponentName(this, WeatherAppWidgetProvider::class.java)

        appWidgetManager.updateAppWidget(provider, views)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, getString(R.string.channel_id))
            .setSmallIcon(R.drawable.baseline_wb_sunny_24)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.channel_text))
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            getString(R.string.channel_id),
            getString(R.string.app_name),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = getString(R.string.channel_description)
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val REQUEST_WEATHER_INFO = 1
        const val REQUEST_SETTING_ACTIVITY = 2
    }
}