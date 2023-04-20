package com.example.weather

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import com.example.weather.UpdateWeatherService.Companion.REQUEST_WEATHER_INFO

class WeatherAppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        appWidgetIds.forEach { appWidgetId ->
            val pendingIntent = Intent(context, UpdateWeatherService::class.java).let {
                PendingIntent.getService(
                    context,
                    REQUEST_WEATHER_INFO,
                    it,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }
            val views = RemoteViews(context.packageName, R.layout.widget_weather).apply {
                setOnClickPendingIntent(R.id.widgetLinearLayout, pendingIntent)
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        val intent = Intent(context, UpdateWeatherService::class.java)
        ContextCompat.startForegroundService(context, intent)
    }
}