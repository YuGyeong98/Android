package com.example.weather

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices

class UpdateWeatherService : Service() {
    private lateinit var appWidgetManager: AppWidgetManager
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        appWidgetManager = AppWidgetManager.getInstance(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // todo 권한 허용 안되어 있으면 setting 액티비티로 넘어가기
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

    private fun updateAppWidget(
        requestCode: Int,
        temperature: CharSequence,
        weather: CharSequence
    ) {
        val pendingIntent = Intent(this, UpdateWeatherService::class.java).let {
            PendingIntent.getService(this, requestCode, it, PendingIntent.FLAG_IMMUTABLE)
        }
        val views = RemoteViews(packageName, R.layout.widget_weather).apply {
            setTextViewText(R.id.temperatureTextView, temperature)
            setTextViewText(R.id.weatherTextView, weather)
            setOnClickPendingIntent(R.id.widgetLinearLayout, pendingIntent)
        }
        val provider = ComponentName(this, WeatherAppWidgetProvider::class.java)

        appWidgetManager.updateAppWidget(provider, views)
    }

    companion object {
        const val REQUEST_WEATHER_INFO = 1
        const val REQUEST_SETTING_ACTIVITY = 2
    }
}